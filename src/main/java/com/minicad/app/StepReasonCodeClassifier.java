package com.minicad.app;

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
     * @param ex the exception (unused but kept for API compatibility)
     * @param reason the reason string to classify
     * @return the classified reason code
     */
    public static String classifyReasonCode(Exception ex, String reason) {
        // Surface-related reasons
        if (reason.contains("construction for CYLINDRICAL_SURFACE is unsupported")) {
            return "unsupported_surface.cylindrical";
        }
        if (reason.contains("construction for CONICAL_SURFACE is unsupported")) {
            return "unsupported_surface.conical";
        }
        if (reason.contains("construction for TOROIDAL_SURFACE is unsupported")) {
            return "unsupported_surface.toroidal";
        }
        if (reason.contains("construction for DEGENERATE_TOROIDAL_SURFACE is unsupported")) {
            return "unsupported_surface.degenerate_toroidal";
        }
        if (reason.contains("construction for B_SPLINE_SURFACE_WITH_KNOTS is unsupported")) {
            return "unsupported_surface.bspline";
        }
        if (reason.contains("construction for RATIONAL_B_SPLINE_SURFACE is unsupported")) {
            return "unsupported_surface.rational_bspline";
        }
        if (reason.contains("construction for RECTANGULAR_TRIMMED_SURFACE is unsupported")) {
            return "unsupported_surface.rectangular_trimmed";
        }
        if (reason.contains("construction for CURVE_BOUNDED_SURFACE is unsupported")) {
            return "unsupported_surface.curve_bounded";
        }
        if (reason.contains("construction for ORIENTED_SURFACE is unsupported")) {
            return "unsupported_surface.oriented";
        }
        if (reason.contains("SURFACE_REPLICA zero scale is unsupported")) {
            return "unsupported_surface.replica_zero_scale";
        }
        if (reason.contains("SURFACE_REPLICA non-uniform scale is unsupported")) {
            return "unsupported_surface.replica_non_uniform_scale";
        }
        if (reason.contains("construction for SURFACE_REPLICA zero scale is unsupported")) {
            return "unsupported_surface.replica_zero_scale";
        }
        if (reason.contains("construction for SURFACE_REPLICA non-uniform scale is unsupported")) {
            return "unsupported_surface.replica_non_uniform_scale";
        }
        if (reason.contains("construction for SURFACE_REPLICA")) {
            return "unsupported_surface.replica";
        }
        if (reason.contains("construction for SURFACE_OF_LINEAR_EXTRUSION is unsupported")) {
            return "unsupported_surface.linear_extrusion";
        }
        if (reason.contains("construction for SURFACE_OF_REVOLUTION is unsupported")) {
            return "unsupported_surface.revolution";
        }
        if (reason.contains("construction for SPHERICAL_SURFACE is unsupported")) {
            return "unsupported_surface.spherical";
        }

        // Curve-related reasons
        if (reason.contains("RATIONAL_B_SPLINE_CURVE is unsupported")) {
            return "unsupported_curve.rational_bspline";
        }
        if (reason.contains("for CURVE_REPLICA is unsupported")) {
            return "unsupported_curve.replica";
        }
        if (reason.contains("OFFSET_CURVE_2D is unsupported")) {
            return "unsupported_curve.offset_2d";
        }
        if (reason.contains("ORIENTED_CURVE is unsupported")) {
            return "unsupported_curve.oriented";
        }
        if (reason.contains("for PARABOLA is unsupported")
                || reason.contains("for HYPERBOLA is unsupported")
                || reason.contains("for DEGENERATE_CONIC is unsupported")) {
            return "unsupported_curve.conic";
        }

        // Boolean-related reasons
        if (reason.contains("BOOLEAN_RESULT construction is unsupported")) {
            return "unsupported_boolean.result";
        }
        if (reason.contains("BOOLEAN_CLIPPING_RESULT construction is unsupported")) {
            return "unsupported_boolean.clipping_result";
        }

        // Topology-related reasons
        if (reason.contains("FACE_BOUND construction for POLY_LOOP is unsupported")) {
            return "unsupported_loop.poly";
        }
        if (reason.contains("must lie on edge curve")) {
            return "topology.edge_vertex_off_curve";
        }
        if (reason.contains("edge loop must be connected and closed")) {
            return "topology.edge_loop_not_closed";
        }
        if (reason.contains("all face vertices must lie on the plane")) {
            return "topology.face_vertex_off_plane";
        }
        if (reason.contains("face must contain an outer bound")) {
            return "topology.face_missing_outer_bound";
        }
        if (reason.contains("requires PLANE geometry")) {
            return "unsupported_surface.non_planar_for_builder";
        }
        if (ex instanceof com.minicad.common.UnsupportedGeometryException) {
            return "unsupported_geometry.other";
        }
        if (ex instanceof com.minicad.common.TopologyException) {
            return "topology.other";
        }
        if (ex instanceof com.minicad.common.StepResolutionException) {
            return "resolution.other";
        }
        if (ex instanceof com.minicad.common.GeometryException) {
            return "geometry.other";
        }
        return "unknown";
    }

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