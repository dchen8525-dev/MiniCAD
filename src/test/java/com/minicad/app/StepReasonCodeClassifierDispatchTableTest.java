package com.minicad.app;

import com.minicad.common.GeometryException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepReasonCodeClassifier.classifyReasonCode.
 *
 * classifyReasonCode used to be a ~34-branch if-chain: 30 {@code
 * reason.contains(...)} checks (one of them an OR of three conic fragments)
 * followed by four {@code ex instanceof ...} checks, plus a {@code "unknown"}
 * tail. It is now the ordered {@code REASON_CODE_RULES} table on
 * StepReasonCodeClassifier.
 *
 * Because the predicates are substring matches, the frozen unit is the returned
 * code, not a class simpleName; the table is read back reflectively (the field
 * and the rule record are private) and its {@code code()} values are compared
 * with src/test/resources/reason-code-dispatch-order.txt. Order matters: the
 * generic {@code "construction for SURFACE_REPLICA"} rule must stay behind the
 * zero-scale / non-uniform-scale variants.
 *
 * The method had no direct test before this fold, so the behaviour cases below
 * are new coverage.
 */
class StepReasonCodeClassifierDispatchTableTest {

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/reason-code-dispatch-order.txt");

    @Test
    @DisplayName("classifyReasonCode keeps the original rule order and codes")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenCodes();
        List<String> actual = liveCodes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/codes changed. The table is ordered data, not "
                        + "control flow: the first match wins, so reordering silently "
                        + "changes which reason code an error is classified as.");
    }

    @Test
    @DisplayName("every reason fragment is classified by its own rule")
    void shouldClassifyReasonFragments() {
        Map<String, String> cases = new LinkedHashMap<>();
        cases.put("construction for CYLINDRICAL_SURFACE is unsupported", "unsupported_surface.cylindrical");
        cases.put("construction for CONICAL_SURFACE is unsupported", "unsupported_surface.conical");
        cases.put("construction for TOROIDAL_SURFACE is unsupported", "unsupported_surface.toroidal");
        cases.put("construction for DEGENERATE_TOROIDAL_SURFACE is unsupported", "unsupported_surface.degenerate_toroidal");
        cases.put("construction for B_SPLINE_SURFACE_WITH_KNOTS is unsupported", "unsupported_surface.bspline");
        cases.put("construction for RATIONAL_B_SPLINE_SURFACE is unsupported", "unsupported_surface.rational_bspline");
        cases.put("construction for RECTANGULAR_TRIMMED_SURFACE is unsupported", "unsupported_surface.rectangular_trimmed");
        cases.put("construction for CURVE_BOUNDED_SURFACE is unsupported", "unsupported_surface.curve_bounded");
        cases.put("construction for ORIENTED_SURFACE is unsupported", "unsupported_surface.oriented");
        cases.put("SURFACE_REPLICA zero scale is unsupported", "unsupported_surface.replica_zero_scale");
        cases.put("SURFACE_REPLICA non-uniform scale is unsupported", "unsupported_surface.replica_non_uniform_scale");
        cases.put("construction for SURFACE_REPLICA zero scale is unsupported", "unsupported_surface.replica_zero_scale");
        cases.put("construction for SURFACE_REPLICA non-uniform scale is unsupported", "unsupported_surface.replica_non_uniform_scale");
        cases.put("construction for SURFACE_OF_LINEAR_EXTRUSION is unsupported", "unsupported_surface.linear_extrusion");
        cases.put("construction for SURFACE_OF_REVOLUTION is unsupported", "unsupported_surface.revolution");
        cases.put("construction for SPHERICAL_SURFACE is unsupported", "unsupported_surface.spherical");
        cases.put("RATIONAL_B_SPLINE_CURVE is unsupported", "unsupported_curve.rational_bspline");
        cases.put("for CURVE_REPLICA is unsupported", "unsupported_curve.replica");
        cases.put("OFFSET_CURVE_2D is unsupported", "unsupported_curve.offset_2d");
        cases.put("ORIENTED_CURVE is unsupported", "unsupported_curve.oriented");
        cases.put("for PARABOLA is unsupported", "unsupported_curve.conic");
        cases.put("for HYPERBOLA is unsupported", "unsupported_curve.conic");
        cases.put("for DEGENERATE_CONIC is unsupported", "unsupported_curve.conic");
        cases.put("BOOLEAN_RESULT construction is unsupported", "unsupported_boolean.result");
        cases.put("BOOLEAN_CLIPPING_RESULT construction is unsupported", "unsupported_boolean.clipping_result");
        cases.put("FACE_BOUND construction for POLY_LOOP is unsupported", "unsupported_loop.poly");
        cases.put("must lie on edge curve", "topology.edge_vertex_off_curve");
        cases.put("edge loop must be connected and closed", "topology.edge_loop_not_closed");
        cases.put("all face vertices must lie on the plane", "topology.face_vertex_off_plane");
        cases.put("face must contain an outer bound", "topology.face_missing_outer_bound");
        cases.put("requires PLANE geometry", "unsupported_surface.non_planar_for_builder");

        for (Map.Entry<String, String> entry : cases.entrySet()) {
            assertEquals(entry.getValue(),
                    StepReasonCodeClassifier.classifyReasonCode(new Exception("probe"), entry.getKey()),
                    "reason fragment not classified as expected: " + entry.getKey());
        }
    }

    @Test
    @DisplayName("the specific SURFACE_REPLICA rules win over the generic one")
    void specificReplicaRulesShouldPrecedeGeneric() {
        assertEquals("unsupported_surface.replica_non_uniform_scale",
                StepReasonCodeClassifier.classifyReasonCode(new Exception("probe"),
                        "construction for SURFACE_REPLICA non-uniform scale is unsupported"));
        assertEquals("unsupported_surface.replica_zero_scale",
                StepReasonCodeClassifier.classifyReasonCode(new Exception("probe"),
                        "construction for SURFACE_REPLICA zero scale is unsupported"));
        assertEquals("unsupported_surface.replica",
                StepReasonCodeClassifier.classifyReasonCode(new Exception("probe"),
                        "construction for SURFACE_REPLICA requires a unit axis"));
    }

    @Test
    @DisplayName("exception type decides when no reason fragment matches")
    void shouldClassifyByExceptionType() {
        assertEquals("unsupported_geometry.other",
                StepReasonCodeClassifier.classifyReasonCode(new UnsupportedGeometryException("x"), ""));
        assertEquals("topology.other",
                StepReasonCodeClassifier.classifyReasonCode(new TopologyException("x"), ""));
        assertEquals("resolution.other",
                StepReasonCodeClassifier.classifyReasonCode(new StepResolutionException("x"), ""));
        assertEquals("geometry.other",
                StepReasonCodeClassifier.classifyReasonCode(new GeometryException("x"), ""));
    }

    @Test
    @DisplayName("an unmatched pair falls through to unknown")
    void shouldFallThroughToUnknown() {
        assertEquals("unknown",
                StepReasonCodeClassifier.classifyReasonCode(new IllegalStateException("x"), "nothing matches"));
        assertEquals("unknown",
                StepReasonCodeClassifier.classifyReasonCode(null, "nothing matches"));
    }

    private static List<String> frozenCodes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath());
        }
        List<String> codes = new ArrayList<>();
        for (String line : Files.readAllLines(FROZEN_ORDER, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                codes.add(trimmed);
            }
        }
        return codes;
    }

    private static List<String> liveCodes() throws Exception {
        Field field = StepReasonCodeClassifier.class.getDeclaredField("REASON_CODE_RULES");
        field.setAccessible(true);
        List<?> rules = (List<?>) field.get(null);
        List<String> codes = new ArrayList<>();
        for (Object rule : rules) {
            Method code = rule.getClass().getDeclaredMethod("code");
            code.setAccessible(true);
            codes.add((String) code.invoke(rule));
        }
        return codes;
    }
}
