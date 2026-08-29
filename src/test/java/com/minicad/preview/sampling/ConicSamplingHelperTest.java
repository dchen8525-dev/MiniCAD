package com.minicad.preview.sampling;

import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepDirection;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConicSamplingHelperTest {

    private static final double EPS = 1.0e-9;

    private static double[] identity() {
        return new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
    }

    private static StepAxis2Placement3D placement3D() {
        StepCartesianPoint loc = new StepCartesianPoint(1, "", Arrays.asList(0.0, 0.0, 0.0));
        StepDirection axis = new StepDirection(2, "", Arrays.asList(0.0, 0.0, 1.0));
        StepDirection ref = new StepDirection(3, "", Arrays.asList(1.0, 0.0, 0.0));
        return new StepAxis2Placement3D(4, "", loc, axis, ref);
    }

    // ---- sampleConicCurvePoints (full pipeline) ----

    @Test
    void sampleConicCurvePointsCircle() {
        StepConicCurve curve = new StepConicCurve(100, "", placement3D(), Arrays.asList(2.0), "CIRCLE");
        List<CartesianPoint> pts = ConicSamplingHelper.sampleConicCurvePoints(curve, null);
        assertEquals(73, pts.size());
    }

    @Test
    void sampleConicCurvePointsEllipse() {
        StepConicCurve curve = new StepConicCurve(101, "", placement3D(), Arrays.asList(3.0, 2.0), "ELLIPSE");
        List<CartesianPoint> pts = ConicSamplingHelper.sampleConicCurvePoints(curve, null);
        assertEquals(73, pts.size());
    }

    @Test
    void sampleConicCurvePointsParabola() {
        StepConicCurve curve = new StepConicCurve(102, "", placement3D(), Arrays.asList(1.0), "PARABOLA");
        List<CartesianPoint> pts = ConicSamplingHelper.sampleConicCurvePoints(curve, null);
        assertEquals(97, pts.size());
    }

    @Test
    void sampleConicCurvePointsHyperbola() {
        StepConicCurve curve = new StepConicCurve(103, "", placement3D(), Arrays.asList(2.0, 1.0), "HYPERBOLA");
        List<CartesianPoint> pts = ConicSamplingHelper.sampleConicCurvePoints(curve, null);
        assertEquals(97, pts.size());
    }

    @Test
    void sampleConicCurvePointsDegenerate() {
        StepConicCurve curve = new StepConicCurve(104, "", placement3D(), Arrays.asList(0.0), "DEGENERATE_CONIC");
        List<CartesianPoint> pts = ConicSamplingHelper.sampleConicCurvePoints(curve, null);
        assertEquals(2, pts.size());
    }

    @Test
    void sampleConicCurvePointsUnknownReturnsNull() {
        StepConicCurve curve = new StepConicCurve(105, "", placement3D(), Arrays.asList(1.0), "FOO");
        assertNull(ConicSamplingHelper.sampleConicCurvePoints(curve, null));
    }

    @Test
    void sampleConicCurvePointsNullMatrixReturnsNull() {
        // position is not a placement entity -> matrix null
        StepConicCurve curve = new StepConicCurve(106, "", new StepCartesianPoint(1, "", Arrays.asList(0.0, 0.0, 0.0)),
                Arrays.asList(2.0), "CIRCLE");
        assertNull(ConicSamplingHelper.sampleConicCurvePoints(curve, null));
    }

    // ---- direct methods with explicit matrix ----

    @Test
    void sampleConicPointsInMatrix() {
        List<CartesianPoint> pts = ConicSamplingHelper.sampleConicPointsInMatrix(identity(), 2.0, 3.0, 72);
        assertEquals(73, pts.size());
        CartesianPoint first = pts.get(0);
        assertTrue(Math.abs(first.x() - 2.0) < EPS);
        assertTrue(Math.abs(first.y() - 0.0) < EPS);
    }

    @Test
    void sampleConicCirclePointsEmptyParams() {
        StepConicCurve curve = new StepConicCurve(200, "", placement3D(), Arrays.asList(), "CIRCLE");
        assertNull(ConicSamplingHelper.sampleConicCirclePoints(curve, identity()));
    }

    @Test
    void sampleConicCirclePointsNonPositiveRadius() {
        StepConicCurve curve = new StepConicCurve(201, "", placement3D(), Arrays.asList(0.0), "CIRCLE");
        assertNull(ConicSamplingHelper.sampleConicCirclePoints(curve, identity()));
    }

    @Test
    void sampleConicCirclePointsValid() {
        StepConicCurve curve = new StepConicCurve(202, "", placement3D(), Arrays.asList(5.0), "CIRCLE");
        assertEquals(73, ConicSamplingHelper.sampleConicCirclePoints(curve, identity()).size());
    }

    @Test
    void sampleConicEllipsePointsTooFewParams() {
        StepConicCurve curve = new StepConicCurve(210, "", placement3D(), Arrays.asList(3.0), "ELLIPSE");
        assertNull(ConicSamplingHelper.sampleConicEllipsePoints(curve, identity()));
    }

    @Test
    void sampleConicEllipsePointsNonPositive() {
        StepConicCurve curve = new StepConicCurve(211, "", placement3D(), Arrays.asList(3.0, 0.0), "ELLIPSE");
        assertNull(ConicSamplingHelper.sampleConicEllipsePoints(curve, identity()));
    }

    @Test
    void sampleConicEllipsePointsValid() {
        StepConicCurve curve = new StepConicCurve(212, "", placement3D(), Arrays.asList(3.0, 2.0), "ELLIPSE");
        assertEquals(73, ConicSamplingHelper.sampleConicEllipsePoints(curve, identity()).size());
    }

    @Test
    void sampleParabolaPointsEmptyParams() {
        StepConicCurve curve = new StepConicCurve(220, "", placement3D(), Arrays.asList(), "PARABOLA");
        assertNull(ConicSamplingHelper.sampleParabolaPoints(curve, identity()));
    }

    @Test
    void sampleParabolaPointsNonPositiveFocal() {
        StepConicCurve curve = new StepConicCurve(221, "", placement3D(), Arrays.asList(0.0), "PARABOLA");
        assertNull(ConicSamplingHelper.sampleParabolaPoints(curve, identity()));
    }

    @Test
    void sampleParabolaPointsValid() {
        StepConicCurve curve = new StepConicCurve(222, "", placement3D(), Arrays.asList(1.0), "PARABOLA");
        assertEquals(97, ConicSamplingHelper.sampleParabolaPoints(curve, identity()).size());
    }

    @Test
    void sampleHyperbolaPointsTooFewParams() {
        StepConicCurve curve = new StepConicCurve(230, "", placement3D(), Arrays.asList(2.0), "HYPERBOLA");
        assertNull(ConicSamplingHelper.sampleHyperbolaPoints(curve, identity()));
    }

    @Test
    void sampleHyperbolaPointsNonPositive() {
        StepConicCurve curve = new StepConicCurve(231, "", placement3D(), Arrays.asList(2.0, 0.0), "HYPERBOLA");
        assertNull(ConicSamplingHelper.sampleHyperbolaPoints(curve, identity()));
    }

    @Test
    void sampleHyperbolaPointsValid() {
        StepConicCurve curve = new StepConicCurve(232, "", placement3D(), Arrays.asList(2.0, 1.0), "HYPERBOLA");
        assertEquals(97, ConicSamplingHelper.sampleHyperbolaPoints(curve, identity()).size());
    }
}
