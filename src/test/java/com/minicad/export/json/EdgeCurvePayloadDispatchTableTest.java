package com.minicad.export.json;

import com.minicad.common.StepResolutionException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.preview.payload.EdgeCurvePayload;
import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepEdgePayloadBuilder.edgeCurvePayload, and exercises every rule at runtime.
 *
 * edgeCurvePayload turned each concrete STEP edge-curve entity into the
 * analytic EdgeCurvePayload the JSON export emits (line via the generic
 * sampled payload, circle/ellipse arcs with placement and sweep, the b-spline
 * families, polylines) via a sequential if/else-if chain, with a trailing
 * generic sampledCurvePayload fallback and a `return null` for unsupported
 * curves. It is now an ordered list of (type, handler) rules dispatched
 * first-match-wins.
 *
 * The table and entry method are private, so this test reaches them through
 * reflection -- the same convention as FaceSurfacePayloadDispatchTableTest.
 * The frozen file pins the primary-type order captured from the original
 * chain: instanceof also matches subtypes and the first match wins, so a
 * dropped, duplicated or reordered rule silently changes which curve is
 * payloaded how. The original chain's 8th branch was a dead duplicate StepLine
 * (unreachable behind the first StepLine rule) and is dropped. All primaries
 * are final classes implementing StepEntity directly (no subtype relation
 * today).
 *
 * The plain StepBSplineCurve rule keeps the original behavior verbatim:
 * builder.buildBSplineCurve requires a StepBSplineCurveWithKnots internally,
 * so the rule throws StepResolutionException, which the entry method's
 * GeometryException|TopologyException catch does NOT swallow. The test pins
 * that it propagates.
 *
 * Fixtures: one resolved document holding every edge-curve kind. The circle
 * arc runs from (2,0,0) to (0,2,0) (startAngle 0, sweep PI/2, open), making
 * the arcSweep slot mapping observable.
 */
class EdgeCurvePayloadDispatchTableTest {

    private static final String TABLE_FIELD = "EDGE_CURVE_PAYLOAD_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/edge-curve-payload-dispatch-order.txt");

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/export/json/StepEdgePayloadBuilder.java";

    private static final String STEP =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
            + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
            + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
            + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
            + "#5=LINE('L0',#1,#10);\n"
            + "#6=CIRCLE('C0',#4,2.0);\n"
            + "#7=ELLIPSE('E0',#4,4.0,2.0);\n"
            + "#8=(B_SPLINE_CURVE('B0',2,(#20,#21,#22),.UNSPECIFIED.,.F.,.F.)\n"
            + "     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.));\n"
            + "#9=(B_SPLINE_CURVE('BS0',2,(#20,#21,#22),.UNSPECIFIED.,.F.,.F.));\n"
            + "#10=VECTOR('VZ',#2,1.0);\n"
            + "#15=(B_SPLINE_CURVE('RB0',2,(#20,#21,#22),.UNSPECIFIED.,.F.,.F.)\n"
            + "     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)\n"
            + "     RATIONAL_B_SPLINE_CURVE((1.0,1.0,1.0)));\n"
            + "#16=POLYLINE('PL0',(#20,#21,#22));\n"
            + "#20=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
            + "#21=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
            + "#22=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
            + "#23=TRIMMED_CURVE('TC0',#6,(#20),(#22),.T.,.CARTESIAN.);\n"
            + "#24=CARTESIAN_POINT('PT',(0.0,0.0,0.0));\n"
            + "ENDSEC;\n";

    private static StepCadBuilder builder;
    private static Map<Integer, StepEntity> resolved;

    @BeforeAll
    static void setUp() {
        resolved = StepEntityResolver.resolveAll(StepParser.parse(STEP));
        builder = StepCadBuilder.fromResolved(resolved);
    }

    private static EdgeCurvePayload payload(int id, CartesianPoint start, CartesianPoint end, boolean naturalForward)
            throws Exception {
        Method method = StepEdgePayloadBuilder.class.getDeclaredMethod(
                "edgeCurvePayload",
                StepEntity.class, CartesianPoint.class, CartesianPoint.class, boolean.class, StepCadBuilder.class);
        method.setAccessible(true);
        return (EdgeCurvePayload) method.invoke(null, resolved.get(id), start, end, naturalForward, builder);
    }

    private static EdgeCurvePayload payload(int id) throws Exception {
        return payload(id, new CartesianPoint(2.0, 0.0, 0.0), new CartesianPoint(0.0, 2.0, 0.0), true);
    }

    // ─── guard: table order, wiring, and no chain creep-back ─────────────

    @Test
    @DisplayName("edgeCurvePayload dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which curve is payloaded how.");
    }

    @Test
    @DisplayName("edgeCurvePayload dispatch table has no duplicate primary types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate primary types in EDGE_CURVE_PAYLOAD_RULES: later entries are "
                        + "unreachable, because the first match returns.");
    }

    /**
     * The entry method must dispatch through the rule table, not grow
     * instanceof branches back: a chain next to the table would be a second,
     * silently diverging copy of the same dispatch.
     */
    @Test
    @DisplayName("edgeCurvePayload dispatches through the table, not instanceof branches")
    void entryMethodShouldNotContainInstanceofBranches() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        String signature = "private static EdgeCurvePayload edgeCurvePayload(";
        int signatureStart = text.indexOf(signature);
        if (signatureStart < 0) {
            fail("Cannot find method " + signature + " in " + HOST_SOURCE);
        }
        int open = text.indexOf('{', signatureStart);
        int depth = 0;
        for (int i = open; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    String body = text.substring(open, i + 1);
                    assertEquals(false, body.contains("instanceof"),
                            "edgeCurvePayload still contains an instanceof branch; "
                                    + "dispatch must go through EDGE_CURVE_PAYLOAD_RULES.");
                    return;
                }
            }
        }
        fail("Unterminated edgeCurvePayload body in " + HOST_SOURCE);
    }

    // ─── behaviour: one test per rule, plus fallback and unsupported ─────

    @Test
    @DisplayName("StepLine routes to the generic sampled payload with the uppercase type name")
    void line() throws Exception {
        EdgeCurvePayload payload = payload(5, new CartesianPoint(0.0, 0.0, 0.0), new CartesianPoint(0.0, 0.0, 1.0), true);
        assertEquals("LINE", payload.type());
        assertNull(payload.center());
        assertEquals(5, payload.stepId());
    }

    @Test
    @DisplayName("StepCircle produces a circle_arc with placement, radius and quarter sweep")
    void circleArc() throws Exception {
        EdgeCurvePayload payload = payload(6);
        assertEquals("circle_arc", payload.type());
        assertEquals(List.of(0.0, 0.0, 0.0), payload.center());
        assertEquals(List.of(0.0, 0.0, 1.0), payload.axis());
        assertEquals(List.of(1.0, 0.0, 0.0), payload.xDirection());
        assertEquals(2.0, payload.radius(), 1.0e-9);
        assertEquals(0.0, payload.startAngle(), 1.0e-9);
        assertEquals(Math.PI / 2.0, payload.sweepAngle(), 1.0e-9);
    }

    @Test
    @DisplayName("a reversed circle arc sweeps backwards")
    void circleArcReversed() throws Exception {
        EdgeCurvePayload payload = payload(6,
                new CartesianPoint(0.0, 2.0, 0.0), new CartesianPoint(2.0, 0.0, 0.0), false);
        assertEquals("circle_arc", payload.type());
        assertEquals(-Math.PI / 2.0, payload.sweepAngle(), 1.0e-9);
    }

    @Test
    @DisplayName("a closed circle arc (start equals end) sweeps a full turn")
    void circleArcClosed() throws Exception {
        CartesianPoint p = new CartesianPoint(2.0, 0.0, 0.0);
        EdgeCurvePayload payload = payload(6, p, p, true);
        assertEquals("circle_arc", payload.type());
        assertEquals(Math.PI * 2.0, payload.sweepAngle(), 1.0e-9);
    }

    @Test
    @DisplayName("StepEllipse produces an ellipse_arc with semi-axes and quarter sweep")
    void ellipseArc() throws Exception {
        EdgeCurvePayload payload = payload(7,
                new CartesianPoint(4.0, 0.0, 0.0), new CartesianPoint(0.0, 2.0, 0.0), true);
        assertEquals("ellipse_arc", payload.type());
        assertEquals(List.of(0.0, 0.0, 0.0), payload.center());
        assertEquals(4.0, payload.semiAxis1(), 1.0e-9);
        assertEquals(2.0, payload.semiAxis2(), 1.0e-9);
        assertNull(payload.radius());
        assertEquals(0.0, payload.startAngle(), 1.0e-9);
        assertEquals(Math.PI / 2.0, payload.sweepAngle(), 1.0e-9);
    }

    @Test
    @DisplayName("StepBSplineCurveWithKnots produces the analytic bspline_curve payload")
    void bsplineCurveWithKnots() throws Exception {
        EdgeCurvePayload payload = payload(8);
        assertEquals("bspline_curve", payload.type());
        assertEquals(8, payload.stepId());
        assertNull(payload.center());
        assertEquals(0.0, payload.startAngle(), 1.0e-9);
        assertEquals(0.0, payload.sweepAngle(), 1.0e-9);
    }

    @Test
    @DisplayName("a plain StepBSplineCurve keeps the original StepResolutionException propagation")
    void plainBsplineCurvePropagatesStepResolutionException() {
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> payload(9));
        assertInstanceOf(StepResolutionException.class, ex.getCause(),
                "buildBSplineCurve requires B_SPLINE_CURVE_WITH_KNOTS; the rule must keep "
                        + "throwing exactly like the original branch (the catch only swallows "
                        + "GeometryException and TopologyException).");
    }

    @Test
    @DisplayName("StepRationalBSplineCurve produces the rational_bspline_curve payload")
    void rationalBsplineCurve() throws Exception {
        EdgeCurvePayload payload = payload(15);
        assertEquals("rational_bspline_curve", payload.type());
        assertEquals(15, payload.stepId());
    }

    @Test
    @DisplayName("StepPolyline produces the polyline payload")
    void polyline() throws Exception {
        EdgeCurvePayload payload = payload(16);
        assertEquals("polyline", payload.type());
        assertEquals(16, payload.stepId());
    }

    @Test
    @DisplayName("a curve with no rule falls through to the generic sampled payload")
    void unsupportedCurveFallsThroughToGenericPayload() throws Exception {
        EdgeCurvePayload payload = payload(23);
        assertEquals("TRIMMED_CURVE", payload.type());
        assertEquals("CIRCLE", payload.basisType());
        assertEquals(6, payload.basisStepId().intValue());
    }

    @Test
    @DisplayName("a non-curve with no rule payloads to null")
    void nonCurveReturnsNull() throws Exception {
        assertNull(payload(24));
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
        Field field = StepEdgePayloadBuilder.class.getDeclaredField(TABLE_FIELD);
        field.setAccessible(true);
        List<?> rules = (List<?>) field.get(null);

        List<String> types = new ArrayList<>();
        for (Object rule : rules) {
            Method accessor = rule.getClass().getDeclaredMethod("type");
            accessor.setAccessible(true);
            types.add(((Class<?>) accessor.invoke(rule)).getSimpleName());
        }
        assertTrue(types.size() >= 2, "dispatch table unexpectedly small");
        return types;
    }
}
