package com.minicad.export.mesh;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepMeshExporter.transformSemanticCurve3, and exercises every rule at
 * runtime.
 *
 * transformSemanticCurve3 transformed each concrete 3D curve type through a
 * STEP Cartesian transformation operator (points scale, directions rotate,
 * wrappers recurse and propagate null) via a sequential if/else-if chain, with
 * a trailing `return null` the callers treat as "unsupported". It is now an
 * ordered list of (type, handler) rules.
 *
 * The table and entry method are private, so this test reaches them through
 * reflection -- the same convention as SemanticSurfaceGeometryDispatchTableTest
 * in this package. The frozen file pins the type order captured from the
 * original chain: instanceof matches subtypes and the first match wins, so a
 * dropped, duplicated or reordered rule silently changes which curve is
 * transformed how. All 9 types are final classes implementing Curve3 directly
 * (no subtype relation today), so the order is currently behaviour neutral.
 *
 * Fixtures: operator T3 (#5) has origin (10,0,0), axes X/Y/Z and scale 2.0, so
 * a point (x,y,z) maps to (10+2x, 2y, 2z) and a direction maps without scale.
 * transformPoint3/transformDirection3 resolve the operator's origin and axes
 * through the builder, hence the real resolved StepCadBuilder.
 */
class TransformSemanticCurve3DispatchTableTest {

    private static final String TABLE_FIELD = "TRANSFORM_SEMANTIC_CURVE3_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/transform-semantic-curve3-dispatch-order.txt");

    private static final String STEP =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('O',(10.0,0.0,0.0));\n"
            + "#2=DIRECTION('AX',(1.0,0.0,0.0));\n"
            + "#3=DIRECTION('AY',(0.0,1.0,0.0));\n"
            + "#4=DIRECTION('AZ',(0.0,0.0,1.0));\n"
            + "#5=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#2,#3,#1,2.0,#4);\n"
            + "ENDSEC;\n";

    private static StepCadBuilder builder;
    private static StepCartesianTransformationOperator t3;

    @BeforeAll
    static void setUp() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(STEP));
        builder = StepCadBuilder.fromResolved(resolved);
        t3 = (StepCartesianTransformationOperator) resolved.get(5);
    }

    private static Curve3 transform(Curve3 curve) throws Exception {
        Method method = StepMeshExporter.class.getDeclaredMethod(
                "transformSemanticCurve3", Curve3.class,
                StepCartesianTransformationOperator.class, StepCadBuilder.class);
        method.setAccessible(true);
        return (Curve3) method.invoke(null, curve, t3, builder);
    }

    private static void assertPoint(CartesianPoint point, double x, double y, double z) {
        assertEquals(x, point.getX(), 1.0e-9);
        assertEquals(y, point.getY(), 1.0e-9);
        assertEquals(z, point.getZ(), 1.0e-9);
    }

    private static Axis2Placement3D placementAtOrigin() {
        return new Axis2Placement3D(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1), new Direction3(1, 0, 0));
    }

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("transformSemanticCurve3 dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which curve is transformed.");
    }

    @Test
    @DisplayName("transformSemanticCurve3 dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in TRANSFORM_SEMANTIC_CURVE3_RULES: later entries are "
                        + "unreachable, because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the fallback ─────────────────

    @Test
    @DisplayName("Line3 transforms origin, rotates direction, scales parameterization")
    void line3() throws Exception {
        Line3 line = new Line3(new CartesianPoint(1, 2, 3), new Direction3(1, 0, 0), 4.0);
        Line3 transformed = assertInstanceOf(Line3.class, transform(line));
        assertPoint(transformed.origin(), 12.0, 4.0, 6.0);
        assertEquals(1.0, transformed.direction().x(), 1.0e-9);
        assertEquals(8.0, transformed.parameterScale(), 1.0e-9);
    }

    @Test
    @DisplayName("Circle transforms placement and scales radius")
    void circle() throws Exception {
        Circle circle = new Circle(placementAtOrigin(), 3.0);
        Circle transformed = assertInstanceOf(Circle.class, transform(circle));
        assertEquals(6.0, transformed.radius(), 1.0e-9);
        assertPoint(transformed.position().location(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("Ellipse3 scales both semi axes")
    void ellipse3() throws Exception {
        Ellipse3 ellipse = new Ellipse3(placementAtOrigin(), 3.0, 2.0);
        Ellipse3 transformed = assertInstanceOf(Ellipse3.class, transform(ellipse));
        assertEquals(6.0, transformed.semiAxis1(), 1.0e-9);
        assertEquals(4.0, transformed.semiAxis2(), 1.0e-9);
    }

    @Test
    @DisplayName("Polyline3 transforms every point")
    void polyline3() throws Exception {
        Polyline3 polyline = new Polyline3(List.of(
                new CartesianPoint(0, 0, 0), new CartesianPoint(1, 1, 1)));
        Polyline3 transformed = assertInstanceOf(Polyline3.class, transform(polyline));
        assertPoint(transformed.points().get(0), 10.0, 0.0, 0.0);
        assertPoint(transformed.points().get(1), 12.0, 2.0, 2.0);
    }

    @Test
    @DisplayName("BSplineCurve3 transforms control points, keeps knots")
    void bsplineCurve3() throws Exception {
        BSplineCurve3 spline = new BSplineCurve3(
                1, List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve3 transformed = assertInstanceOf(BSplineCurve3.class, transform(spline));
        assertPoint(transformed.controlPoints().get(0), 10.0, 0.0, 0.0);
        assertPoint(transformed.controlPoints().get(1), 12.0, 0.0, 0.0);
        assertEquals(List.of(0.0, 1.0), transformed.knots());
    }

    @Test
    @DisplayName("RationalBSplineCurve3 transforms control points, keeps weights and knots")
    void rationalBsplineCurve3() throws Exception {
        RationalBSplineCurve3 spline = new RationalBSplineCurve3(
                1, List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0)),
                List.of(1.0, 2.0), List.of(2, 2), List.of(0.0, 1.0));
        RationalBSplineCurve3 transformed = assertInstanceOf(RationalBSplineCurve3.class, transform(spline));
        assertPoint(transformed.controlPoints().get(0), 10.0, 0.0, 0.0);
        assertEquals(List.of(1.0, 2.0), transformed.weights());
    }

    @Test
    @DisplayName("CompositeCurve3 transforms every segment")
    void compositeCurve3() throws Exception {
        CompositeCurve3 composite = new CompositeCurve3(List.of(
                new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0), 1.0),
                new Line3(new CartesianPoint(1, 0, 0), new Direction3(0, 1, 0), 1.0)));
        CompositeCurve3 transformed = assertInstanceOf(CompositeCurve3.class, transform(composite));
        assertEquals(2, transformed.segments().size());
        assertPoint(((Line3) transformed.segments().get(0)).origin(), 10.0, 0.0, 0.0);
        assertPoint(((Line3) transformed.segments().get(1)).origin(), 12.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("TrimmedCurve3 transforms its basis, keeps trim parameters")
    void trimmedCurve3() throws Exception {
        TrimmedCurve3 trimmed = new TrimmedCurve3(
                new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0), 1.0),
                0.25, 0.75, true);
        TrimmedCurve3 transformed = assertInstanceOf(TrimmedCurve3.class, transform(trimmed));
        assertPoint(((Line3) transformed.basisCurve()).origin(), 10.0, 0.0, 0.0);
        assertEquals(0.25, transformed.trimParamStart(), 1.0e-9);
        assertEquals(0.75, transformed.trimParamEnd(), 1.0e-9);
    }

    @Test
    @DisplayName("SurfaceCurve3 transforms its 3D curve, keeps parametric bindings")
    void surfaceCurve3() throws Exception {
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(
                new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0), 1.0));
        SurfaceCurve3 transformed = assertInstanceOf(SurfaceCurve3.class, transform(surfaceCurve));
        assertPoint(((Line3) transformed.curve3d()).origin(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("a curve type with no rule transforms to null")
    void unsupportedTypeReturnsNull() throws Exception {
        assertNull(transform(new DegenerateCurve3(new CartesianPoint(1, 2, 3))));
    }

    @Test
    @DisplayName("CompositeCurve3 propagates null from an unsupported segment")
    void compositePropagatesNull() throws Exception {
        CompositeCurve3 composite = new CompositeCurve3(List.of(
                new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0), 1.0),
                new DegenerateCurve3(new CartesianPoint(1, 1, 1))));
        assertNull(transform(composite));
    }

    // ─── reflection helpers ──────────────────────────────────────────────

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath());
        }
        List<String> types = new ArrayList<>();
        for (String line : Files.readAllLines(FROZEN_ORDER, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                types.add(trimmed);
            }
        }
        return types;
    }

    private static List<String> liveRuleTypes() throws Exception {
        Field field = StepMeshExporter.class.getDeclaredField(TABLE_FIELD);
        field.setAccessible(true);
        List<?> rules = (List<?>) field.get(null);

        List<String> types = new ArrayList<>();
        for (Object rule : rules) {
            Method accessor = rule.getClass().getDeclaredMethod("type");
            accessor.setAccessible(true);
            types.add(((Class<?>) accessor.invoke(rule)).getSimpleName());
        }
        return types;
    }
}
