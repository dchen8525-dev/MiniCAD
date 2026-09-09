package com.minicad.step.semantic;

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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepCadTopologyBuilder.projectOntoCurve, and exercises every rule at runtime.
 *
 * projectOntoCurve used to be a 9-branch sequential if/else-if chain that
 * projected a point onto each concrete curve type (closest-point for splines,
 * analytic projection for lines and circles, sampling for ellipses and
 * polylines, delegation through wrappers), with a trailing `return point`
 * fallback the Edge constructor later validates. It is now an ordered list of
 * (type, handler) rules.
 *
 * The table and the entry method are private, so this test reaches them
 * through reflection -- the same convention as the other private-dispatch
 * guards in this package (TypeNameDispatchTableTest, AssignmentDispatchTableTest).
 * The frozen file pins the type order captured from the original chain:
 * instanceof matches subtypes and the first match wins, so a dropped,
 * duplicated or reordered rule -- or a rule pushed past the fallback boundary
 * -- silently changes which curve is projected how. All 9 types are final
 * classes implementing Curve3 directly (no subtype relation today), so the
 * order is currently behaviour neutral; the frozen file turns any future
 * reordering into a test failure.
 */
class StepCadTopologyBuilderProjectOntoCurveTest {

    private static final String TABLE_FIELD = "PROJECT_ONTO_CURVE_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/project-onto-curve-dispatch-order.txt");

    private static final double EPS = 1.0e-9;

    private static CartesianPoint p3(double x, double y, double z) {
        return new CartesianPoint(x, y, z);
    }

    private static Axis2Placement3D xyPlane() {
        return new Axis2Placement3D(p3(0, 0, 0), new Direction3(0, 0, 1), new Direction3(1, 0, 0));
    }

    private static CartesianPoint project(CartesianPoint point, Curve3 curve) throws Exception {
        Method method = StepCadTopologyBuilder.class.getDeclaredMethod(
                "projectOntoCurve", CartesianPoint.class, Curve3.class);
        method.setAccessible(true);
        return (CartesianPoint) method.invoke(null, point, curve);
    }

    private static void assertPoint(CartesianPoint point, double x, double y, double z) {
        assertEquals(x, point.getX(), 1.0e-6);
        assertEquals(y, point.getY(), 1.0e-6);
        assertEquals(z, point.getZ(), 1.0e-6);
    }

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("projectOntoCurve dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which curve is projected how.");
    }

    @Test
    @DisplayName("projectOntoCurve dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in PROJECT_ONTO_CURVE_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the fallback ─────────────────

    @Test
    @DisplayName("Line3 projects analytically onto the infinite line")
    void line3() throws Exception {
        Line3 line = new Line3(p3(0, 0, 0), new Direction3(1, 0, 0), 1.0);
        assertPoint(project(p3(3.0, 5.0, -2.0), line), 3.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("Circle projects radially onto the circumference")
    void circle() throws Exception {
        Circle circle = new Circle(xyPlane(), 2.0);
        assertPoint(project(p3(5.0, 0.0, 0.0), circle), 2.0, 0.0, 0.0);
        assertPoint(project(p3(0.0, 5.0, 0.0), circle), 0.0, 2.0, 0.0);
    }

    @Test
    @DisplayName("Circle at its exact center falls back to the x-direction point")
    void circleAtCenter() throws Exception {
        Circle circle = new Circle(xyPlane(), 2.0);
        assertPoint(project(p3(0.0, 0.0, 0.0), circle), 2.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("Ellipse3 projects by sampling and lands on the curve")
    void ellipse3() throws Exception {
        Ellipse3 ellipse = new Ellipse3(xyPlane(), 3.0, 2.0);
        CartesianPoint projected = project(p3(6.0, 0.0, 0.0), ellipse);
        assertPoint(projected, 3.0, 0.0, 0.0);
        assertTrue(ellipse.contains(projected), "projected point must lie on the ellipse");
    }

    @Test
    @DisplayName("BSplineCurve3 projects to its closest sampled point")
    void bsplineCurve3() throws Exception {
        BSplineCurve3 spline = new BSplineCurve3(
                1, List.of(p3(0, 0, 0), p3(10, 0, 0)), List.of(2, 2), List.of(0.0, 1.0));
        CartesianPoint projected = project(p3(5.0, 3.0, 0.0), spline);
        assertTrue(spline.contains(projected), "projected point must lie on the spline");
        assertEquals(0.0, projected.getZ(), EPS);
        assertTrue(Math.abs(projected.getX() - 5.0) < 0.5, "closest sample is near x=5");
    }

    @Test
    @DisplayName("RationalBSplineCurve3 projects to its closest sampled point")
    void rationalBsplineCurve3() throws Exception {
        RationalBSplineCurve3 spline = new RationalBSplineCurve3(
                1, List.of(p3(0, 0, 0), p3(10, 0, 0)), List.of(1.0, 1.0), List.of(2, 2), List.of(0.0, 1.0));
        CartesianPoint projected = project(p3(5.0, 3.0, 0.0), spline);
        assertTrue(spline.contains(projected), "projected point must lie on the spline");
        assertTrue(Math.abs(projected.getX() - 5.0) < 0.5);
    }

    @Test
    @DisplayName("Polyline3 projects onto its closest segment")
    void polyline3() throws Exception {
        Polyline3 polyline = new Polyline3(List.of(p3(0, 0, 0), p3(10, 0, 0), p3(10, 10, 0)));
        // closest to (4,3,0) is (4,0,0) on the first segment
        assertPoint(project(p3(4.0, 3.0, 0.0), polyline), 4.0, 0.0, 0.0);
        // closest to (12,6,0) is (10,6,0) on the second segment
        assertPoint(project(p3(12.0, 6.0, 0.0), polyline), 10.0, 6.0, 0.0);
    }

    @Test
    @DisplayName("TrimmedCurve3 projects through to its basis curve")
    void trimmedCurve3() throws Exception {
        Circle basis = new Circle(xyPlane(), 2.0);
        TrimmedCurve3 trimmed = new TrimmedCurve3(basis, 0.0, Math.PI, true);
        assertPoint(project(p3(5.0, 0.0, 0.0), trimmed), 2.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("SurfaceCurve3 projects through to its wrapped 3D curve")
    void surfaceCurve3() throws Exception {
        Line3 basis = new Line3(p3(0, 0, 0), new Direction3(1, 0, 0), 1.0);
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(basis);
        assertPoint(project(p3(3.0, 5.0, -2.0), surfaceCurve), 3.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("CompositeCurve3 picks the closest projection across segments")
    void compositeCurve3() throws Exception {
        Line3 a = new Line3(p3(0, 0, 0), new Direction3(1, 0, 0), 1.0);
        Line3 b = new Line3(p3(0, 5, 0), new Direction3(1, 0, 0), 1.0);
        CompositeCurve3 composite = new CompositeCurve3(List.of(a, b));
        // (4, 1, 0) is closer to segment a (y=0) than b (y=5)
        assertPoint(project(p3(4.0, 1.0, 0.0), composite), 4.0, 0.0, 0.0);
        // (4, 4, 0) is closer to segment b (y=5)
        assertPoint(project(p3(4.0, 4.0, 0.0), composite), 4.0, 5.0, 0.0);
    }

    @Test
    @DisplayName("a curve type with no rule returns the point unchanged")
    void unsupportedTypeFallsBackToInput() throws Exception {
        DegenerateCurve3 degenerate = new DegenerateCurve3(p3(1, 2, 3));
        CartesianPoint point = p3(7.0, 8.0, 9.0);
        assertPoint(project(point, degenerate), 7.0, 8.0, 9.0);
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
        Field field = StepCadTopologyBuilder.class.getDeclaredField(TABLE_FIELD);
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
