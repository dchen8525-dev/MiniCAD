package com.minicad.preview.sampling;

import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runtime behaviour tests for the LOOSE_EDGE_POINTS_RULES dispatch in
 * PreviewCurveEvaluator.sampleLooseEdgePoints, exercised through real STEP
 * entities resolved into a real StepCadBuilder (the CURVE_REPLICA branch
 * transforms every sampled point through the replica's
 * CARTESIAN_TRANSFORMATION_OPERATOR_3D, whose origin and axes are resolved
 * through the builder).
 *
 * Fixture geometry: LINE #4 runs from (0,0,0) along +X with parameter scale 5.
 * Line3.sample covers a fixed world-space range of +/-10 along the line, so
 * its loose sample runs from (-10,0,0) to (10,0,0). Operator #8 translates by
 * (10,0,0) and scales by 2.0 with identity axes, so replica points map to
 * (10+2x, 2y, 2z): the sample becomes (-10,0,0) .. (30,0,0).
 */
class PreviewLooseEdgePointsSamplingTest {

    private static final String STEP =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
            + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
            + "#3=VECTOR('V0',#2,5.0);\n"
            + "#4=LINE('L0',#1,#3);\n"
            + "#5=DIRECTION('AX',(1.0,0.0,0.0));\n"
            + "#6=DIRECTION('AY',(0.0,1.0,0.0));\n"
            + "#7=CARTESIAN_POINT('O',(10.0,0.0,0.0));\n"
            + "#8=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T',#5,#6,#7,2.0,$);\n"
            + "#9=CURVE_REPLICA('CR',#4,#8);\n"
            + "#10=ORIENTED_CURVE('OC',#4,.F.);\n"
            + "#11=GEOMETRIC_SET('GS',(#4,#10));\n"
            + "ENDSEC;\n";

    private static StepCadBuilder builder;
    private static StepEntity line;
    private static StepGeometricReplica curveReplica;
    private static StepOrientedCurve orientedCurve;
    private static StepGeometricSet geometricSet;

    @BeforeAll
    static void setUp() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(STEP));
        builder = StepCadBuilder.fromResolved(resolved);
        line = resolved.get(4);
        curveReplica = (StepGeometricReplica) resolved.get(9);
        orientedCurve = (StepOrientedCurve) resolved.get(10);
        geometricSet = (StepGeometricSet) resolved.get(11);
    }

    private static void assertPoint(CartesianPoint point, double x, double y, double z) {
        assertEquals(x, point.getX(), 1.0e-9);
        assertEquals(y, point.getY(), 1.0e-9);
        assertEquals(z, point.getZ(), 1.0e-9);
    }

    @Test
    @DisplayName("fallback: a LINE with no dedicated rule samples through curveForLooseEdge")
    void lineFallsThroughToCurveSampling() {
        List<CartesianPoint> points = PreviewCurveEvaluator.sampleLooseEdgePoints(line, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 2, "line sample should have at least endpoints");
        assertPoint(points.get(0), -10.0, 0.0, 0.0);
        CartesianPoint last = points.get(points.size() - 1);
        assertEquals(10.0, last.getX(), 1.0e-9);
        assertEquals(0.0, last.getY(), 1.0e-9);
        assertEquals(0.0, last.getZ(), 1.0e-9);
    }

    @Test
    @DisplayName("CURVE_REPLICA transforms every sampled parent point through the operator")
    void curveReplicaTransformsParentSample() {
        List<CartesianPoint> base = PreviewCurveEvaluator.sampleLooseEdgePoints(line, builder);
        List<CartesianPoint> points = PreviewCurveEvaluator.sampleLooseEdgePoints(curveReplica, builder);
        assertNotNull(points);
        assertEquals(base.size(), points.size(), "replica sample must keep the parent sample size");
        assertPoint(points.get(0), -10.0, 0.0, 0.0);
        CartesianPoint last = points.get(points.size() - 1);
        assertEquals(30.0, last.getX(), 1.0e-9);
        assertEquals(0.0, last.getY(), 1.0e-9);
        assertEquals(0.0, last.getZ(), 1.0e-9);
    }

    @Test
    @DisplayName("ORIENTED_CURVE with orientation .F. returns the reversed element sample")
    void orientedCurveReversesElementSample() {
        List<CartesianPoint> base = PreviewCurveEvaluator.sampleLooseEdgePoints(line, builder);
        List<CartesianPoint> points = PreviewCurveEvaluator.sampleLooseEdgePoints(orientedCurve, builder);
        assertNotNull(points);
        assertEquals(base.size(), points.size());
        List<CartesianPoint> expectedReversed = new ArrayList<>(base);
        Collections.reverse(expectedReversed);
        for (int i = 0; i < expectedReversed.size(); i++) {
            assertPoint(points.get(i),
                    expectedReversed.get(i).getX(),
                    expectedReversed.get(i).getY(),
                    expectedReversed.get(i).getZ());
        }
    }

    @Test
    @DisplayName("GEOMETRIC_SET concatenates the samples of its elements")
    void geometricSetConcatenatesElementSamples() {
        List<CartesianPoint> base = PreviewCurveEvaluator.sampleLooseEdgePoints(line, builder);
        List<CartesianPoint> reversed = PreviewCurveEvaluator.sampleLooseEdgePoints(orientedCurve, builder);
        List<CartesianPoint> points = PreviewCurveEvaluator.sampleLooseEdgePoints(geometricSet, builder);
        assertNotNull(points);
        assertEquals(base.size() + reversed.size(), points.size(), "set sample is the element concatenation");
        assertPoint(points.get(0), -10.0, 0.0, 0.0);
        assertPoint(points.get(base.size()), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("same entity instance returns the same dispatch result shape")
    void repeatedSamplingIsStable() {
        List<CartesianPoint> first = PreviewCurveEvaluator.sampleLooseEdgePoints(line, builder);
        List<CartesianPoint> second = PreviewCurveEvaluator.sampleLooseEdgePoints(line, builder);
        assertEquals(first.size(), second.size());
        assertSame(orientedCurve.curveElement(), line);
    }
}
