package com.minicad.app;

import com.minicad.common.GeometryException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;

import java.util.List;

/**
 * Utility class for classifying STEP error reason codes.
 * Extracted from StepDumpApp to improve maintainability.
 *
 * <p>This class provides methods to normalize and classify error messages
 * from STEP processing into structured reason codes.
 */
public final class StepReasonCodeClassifier {

    private StepReasonCodeClassifier() {
        // Utility class - prevent instantiation
    }

    /**
     * Classifies an exception reason into a structured reason code.
     *
     * <p>Dispatch is the ordered {@link #REASON_CODE_RULES} table, first match
     * wins; an unmatched pair falls through to {@code "unknown"} (the original
     * chain's tail). The order is load bearing: some fragments are substrings of
     * later, more general ones (e.g. {@code "construction for SURFACE_REPLICA
     * non-uniform scale is unsupported"} against the generic {@code "construction
     * for SURFACE_REPLICA"}), so the specific rule must stay ahead of the general
     * one.
     *
     * @param ex the exception whose type is consulted by the trailing rules
     * @param reason the reason string to classify
     * @return the classified reason code
     */
    public static String classifyReasonCode(Exception ex, String reason) {
        for (ReasonCodeRule rule : REASON_CODE_RULES) {
            if (rule.matcher().matches(ex, reason)) {
                return rule.code();
            }
        }
        return "unknown";
    }

    private static ReasonCodeRule reasonContains(String code, String fragment) {
        return new ReasonCodeRule((ex, reason) -> reason.contains(fragment), code);
    }

    private static ReasonCodeRule reasonContainsAny(String code, String... fragments) {
        return new ReasonCodeRule((ex, reason) -> {
            for (String fragment : fragments) {
                if (reason.contains(fragment)) {
                    return true;
                }
            }
            return false;
        }, code);
    }

    private static ReasonCodeRule exceptionType(Class<? extends Exception> type, String code) {
        return new ReasonCodeRule((ex, reason) -> type.isInstance(ex), code);
    }

    /**
     * Ordered reason-code dispatch table, first match wins. The original chain was
     * a sequence of {@code if (reason.contains(...))} / {@code if (ex instanceof
     * ...)} branches; the codes and their order are frozen by
     * {@code StepReasonCodeClassifierDispatchTableTest}.
     */
    private static final List<ReasonCodeRule> REASON_CODE_RULES = List.of(
            reasonContains("unsupported_surface.cylindrical", "construction for CYLINDRICAL_SURFACE is unsupported"),
            reasonContains("unsupported_surface.conical", "construction for CONICAL_SURFACE is unsupported"),
            reasonContains("unsupported_surface.toroidal", "construction for TOROIDAL_SURFACE is unsupported"),
            reasonContains("unsupported_surface.degenerate_toroidal", "construction for DEGENERATE_TOROIDAL_SURFACE is unsupported"),
            reasonContains("unsupported_surface.bspline", "construction for B_SPLINE_SURFACE_WITH_KNOTS is unsupported"),
            reasonContains("unsupported_surface.rational_bspline", "construction for RATIONAL_B_SPLINE_SURFACE is unsupported"),
            reasonContains("unsupported_surface.rectangular_trimmed", "construction for RECTANGULAR_TRIMMED_SURFACE is unsupported"),
            reasonContains("unsupported_surface.curve_bounded", "construction for CURVE_BOUNDED_SURFACE is unsupported"),
            reasonContains("unsupported_surface.oriented", "construction for ORIENTED_SURFACE is unsupported"),
            reasonContains("unsupported_surface.replica_zero_scale", "SURFACE_REPLICA zero scale is unsupported"),
            reasonContains("unsupported_surface.replica_non_uniform_scale", "SURFACE_REPLICA non-uniform scale is unsupported"),
            reasonContains("unsupported_surface.replica_zero_scale", "construction for SURFACE_REPLICA zero scale is unsupported"),
            reasonContains("unsupported_surface.replica_non_uniform_scale", "construction for SURFACE_REPLICA non-uniform scale is unsupported"),
            reasonContains("unsupported_surface.replica", "construction for SURFACE_REPLICA"),
            reasonContains("unsupported_surface.linear_extrusion", "construction for SURFACE_OF_LINEAR_EXTRUSION is unsupported"),
            reasonContains("unsupported_surface.revolution", "construction for SURFACE_OF_REVOLUTION is unsupported"),
            reasonContains("unsupported_surface.spherical", "construction for SPHERICAL_SURFACE is unsupported"),
            reasonContains("unsupported_curve.rational_bspline", "RATIONAL_B_SPLINE_CURVE is unsupported"),
            reasonContains("unsupported_curve.replica", "for CURVE_REPLICA is unsupported"),
            reasonContains("unsupported_curve.offset_2d", "OFFSET_CURVE_2D is unsupported"),
            reasonContains("unsupported_curve.oriented", "ORIENTED_CURVE is unsupported"),
            reasonContainsAny("unsupported_curve.conic",
                    "for PARABOLA is unsupported", "for HYPERBOLA is unsupported", "for DEGENERATE_CONIC is unsupported"),
            reasonContains("unsupported_boolean.result", "BOOLEAN_RESULT construction is unsupported"),
            reasonContains("unsupported_boolean.clipping_result", "BOOLEAN_CLIPPING_RESULT construction is unsupported"),
            reasonContains("unsupported_loop.poly", "FACE_BOUND construction for POLY_LOOP is unsupported"),
            reasonContains("topology.edge_vertex_off_curve", "must lie on edge curve"),
            reasonContains("topology.edge_loop_not_closed", "edge loop must be connected and closed"),
            reasonContains("topology.face_vertex_off_plane", "all face vertices must lie on the plane"),
            reasonContains("topology.face_missing_outer_bound", "face must contain an outer bound"),
            reasonContains("unsupported_surface.non_planar_for_builder", "requires PLANE geometry"),
            exceptionType(UnsupportedGeometryException.class, "unsupported_geometry.other"),
            exceptionType(TopologyException.class, "topology.other"),
            exceptionType(StepResolutionException.class, "resolution.other"),
            exceptionType(GeometryException.class, "geometry.other"));

    @FunctionalInterface
    private interface ReasonCodeMatcher {
        boolean matches(Exception ex, String reason);
    }

    private record ReasonCodeRule(ReasonCodeMatcher matcher, String code) {}

    /**
     * Normalizes a reason string by removing line separators and trimming.
     *
     * @param message the raw reason string
     * @return the normalized reason string
     */
    public static String normalizeReason(String message) {
        if (message == null || message.isBlank()) {
            return "unknown";
        }
        return message.replace(System.lineSeparator(), " ").trim();
    }
}
