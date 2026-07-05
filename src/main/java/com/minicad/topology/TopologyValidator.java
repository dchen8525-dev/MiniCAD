package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.MiniCadIssue;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Plane;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Objects;

/**
 * Non-mutating topology checks for imported B-Rep structures.
 */
public final class TopologyValidator {

    private TopologyValidator() {
    }

    /**
     * Validates shell-level edge usage and manifold invariants.
     *
     * @param shell shell to validate
     * @return validation result
     */
    public static ValidationResult validateShell(Shell shell) {
        if (shell == null) {
            return new ValidationResult(List.of(MiniCadIssue.error(
                    "shell.null",
                    null,
                    null,
                    "shell must not be null"
            )));
        }

        List<MiniCadIssue> issues = new ArrayList<>();
        Map<Edge, EdgeUseSummary> edgeUses = new IdentityHashMap<>();
        for (int faceIndex = 0; faceIndex < shell.getFaces().size(); faceIndex++) {
            Face face = shell.getFaces().get(faceIndex);
            validateFace(face, faceIndex, issues);
            for (FaceBound bound : face.getBounds()) {
                if (bound.getLoop() instanceof EdgeLoop) {
                    EdgeLoop edgeLoop = (EdgeLoop) bound.getLoop();
                    for (OrientedEdge orientedEdge : edgeLoop.edges()) {
                        edgeUses.computeIfAbsent(orientedEdge.getEdge(), edge -> new EdgeUseSummary())
                                .add(orientedEdge);
                    }
                }
            }
        }

        for (Map.Entry<Edge, EdgeUseSummary> entry : edgeUses.entrySet()) {
            Edge edge = entry.getKey();
            EdgeUseSummary summary = entry.getValue();
            if (summary.total() > 2) {
                issues.add(MiniCadIssue.error(
                        "shell.non_manifold_edge",
                        null,
                        null,
                        "edge " + describeEdge(edge) + " is used by " + summary.total() + " face bounds"
                ));
            }
            if (shell.isClosed() && summary.total() != 2) {
                issues.add(MiniCadIssue.error(
                        "closed_shell.edge_use_count",
                        null,
                        null,
                        "closed shell edge " + describeEdge(edge) + " is used " + summary.total()
                                + " time(s), expected 2"
                ));
            }
            if (shell.isClosed() && summary.total() == 2
                    && (summary.forwardCount() != 1 || summary.reverseCount() != 1)) {
                issues.add(MiniCadIssue.error(
                        "closed_shell.edge_orientation",
                        null,
                        null,
                        "closed shell edge " + describeEdge(edge)
                                + " must be used once in each orientation"
                ));
            }
        }

        return new ValidationResult(issues);
    }

    /**
     * Validates a solid and its optional void shells.
     *
     * @param solid solid to validate
     * @return validation result
     */
    public static ValidationResult validateSolid(Solid solid) {
        if (solid == null) {
            return new ValidationResult(List.of(MiniCadIssue.error(
                    "solid.null",
                    null,
                    null,
                    "solid must not be null"
            )));
        }

        List<MiniCadIssue> issues = new ArrayList<>();
        issues.addAll(validateShell(solid.getOuterShell()).getIssues());

        BoundingBox3 outerBox = solid.getOuterShell().boundingBox();
        double outerVolumeSign = Math.signum(signedShellVolume(solid.getOuterShell()));
        for (int index = 0; index < solid.getVoidShells().size(); index++) {
            Shell voidShell = solid.getVoidShells().get(index);
            issues.addAll(validateShell(voidShell).getIssues());
            if (!outerBox.contains(voidShell.boundingBox())) {
                issues.add(MiniCadIssue.error(
                        "solid.void_outside_outer",
                        null,
                        null,
                        "void shell " + index + " bounding box must be inside the outer shell bounding box"
                ));
            }

            double voidVolumeSign = Math.signum(signedShellVolume(voidShell));
            if (outerVolumeSign != 0.0 && voidVolumeSign != 0.0 && outerVolumeSign == voidVolumeSign) {
                issues.add(MiniCadIssue.error(
                        "solid.void_orientation",
                        null,
                        null,
                        "void shell " + index + " orientation must be opposite to the outer shell"
                ));
            }
        }

        return new ValidationResult(issues);
    }

    private static void validateFace(Face face, int faceIndex, List<MiniCadIssue> issues) {
        if (face.getSurface() instanceof Plane) {
            double area = planarOuterArea(face);
            if (area <= Epsilon.EPS) {
                issues.add(MiniCadIssue.error(
                        "face.zero_area",
                        null,
                        null,
                        "planar face at shell index " + faceIndex + " has zero area"
                ));
            }
        }

        // D09: Validate FACE_BOUND winding direction (inner bounds must be opposite to outer)
        validateFaceBoundsOrientation(face, faceIndex, issues);

        // D10: Detect zero-length edges in face bounds
        detectZeroLengthEdgesInFace(face, faceIndex, issues);
    }

    private static double planarOuterArea(Face face) {
        FaceBound outer = face.outerBound();
        if (outer == null) {
            return 0.0;
        }
        if (outer.getLoop() instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) outer.getLoop();
            return polygonArea3d(edgeLoop.vertices().stream()
                    .map(Vertex::point)
                    .collect(Collectors.toList()));
        }
        if (outer.getLoop() instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) outer.getLoop();
            return polygonArea3d(polyLoop.getPoints());
        }
        return 0.0;
    }

    private static String describeEdge(Edge edge) {
        return "(" + describeVertex(edge.getStart()) + " -> " + describeVertex(edge.getEnd()) + ")";
    }

    private static String describeVertex(Vertex vertex) {
        return String.format(
                "%.6f,%.6f,%.6f",
                vertex.point().getX(),
                vertex.point().getY(),
                vertex.point().getZ()
        );
    }

    private static double signedShellVolume(Shell shell) {
        double volume = 0.0;
        for (Face face : shell.getFaces()) {
            FaceBound outer = face.outerBound();
            if (outer != null && outer.getLoop() instanceof EdgeLoop) { EdgeLoop edgeLoop = (EdgeLoop) outer.getLoop();
                double contribution = signedLoopVolume(edgeLoop.vertices());
                volume += face.isSameSense() ? contribution : -contribution;
            }
        }
        return volume / 6.0;
    }

    private static double signedLoopVolume(List<Vertex> vertices) {
        if (vertices.size() < 3) {
            return 0.0;
        }
        CartesianPoint origin = vertices.get(0).point();
        double volume = 0.0;
        for (int index = 1; index < vertices.size() - 1; index++) {
            CartesianPoint b = vertices.get(index).point();
            CartesianPoint c = vertices.get(index + 1).point();
            volume += dot(origin, cross(b, c));
        }
        return volume;
    }

    private static double polygonArea3d(List<CartesianPoint> points) {
        if (points.size() < 3) {
            return 0.0;
        }
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (int index = 0; index < points.size(); index++) {
            CartesianPoint current = points.get(index);
            CartesianPoint next = points.get((index + 1) % points.size());
            CartesianPoint cross = cross(current, next);
            x += cross.getX();
            y += cross.getY();
            z += cross.getZ();
        }
        return 0.5 * Math.sqrt(x * x + y * y + z * z);
    }

    private static double dot(CartesianPoint a, CartesianPoint b) {
        return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
    }

    private static CartesianPoint cross(CartesianPoint a, CartesianPoint b) {
        return new CartesianPoint(
                a.getY() * b.getZ() - a.getZ() * b.getY(),
                a.getZ() * b.getX() - a.getX() * b.getZ(),
                a.getX() * b.getY() - a.getY() * b.getX()
        );
    }

    private static final class EdgeUseSummary {
        private int forwardCount;
        private int reverseCount;

        private void add(OrientedEdge edge) {
            if (edge.isOrientation()) {
                forwardCount++;
            } else {
                reverseCount++;
            }
        }

        private int total() {
            return forwardCount + reverseCount;
        }

        private int forwardCount() {
            return forwardCount;
        }

        private int reverseCount() {
            return reverseCount;
        }
    }

    /**
     * Topology validation result.
     *
     * @param issues validation issues
     */
public static final class ValidationResult {
    private final List<MiniCadIssue> issues;

    public ValidationResult(List<MiniCadIssue> issues) {
        this.issues = issues == null ? null : java.util.List.copyOf(issues);
    }

    public List<MiniCadIssue> getIssues() {
        return issues;
    }

    // Record-style accessor
    public List<MiniCadIssue> issues() {
        return issues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationResult that = (ValidationResult) o;
        return Objects.equals(issues, that.issues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issues);
    }

    @Override
    public String toString() {
        return "ValidationResult{" + "issues=" + issues + "}";
    }
}

    // ========================================================================
    // D09: FACE_BOUND winding direction validation
    // ========================================================================

    /**
     * Validates that inner bounds are wound opposite to outer bound.
     * This ensures holes are correctly oriented for proper face definition.
     *
     * @param face face to validate
     * @param faceIndex face index in shell
     * @param issues list to add validation issues
     */
    private static void validateFaceBoundsOrientation(Face face, int faceIndex, List<MiniCadIssue> issues) {
        List<FaceBound> bounds = face.getBounds();
        if (bounds.isEmpty()) {
            return;
        }

        // Find outer bound
        FaceBound outerBound = bounds.stream()
                .filter(FaceBound::outer)
                .findFirst()
                .orElse(null);

        if (outerBound == null) {
            // No outer bound - cannot validate winding direction
            return;
        }

        // Calculate winding direction of outer bound
        WindingDirection outerWinding = calculateWindingDirection(outerBound.getLoop());
        if (outerWinding == WindingDirection.UNKNOWN) {
            // Cannot determine winding direction (e.g., VertexLoop)
            return;
        }

        // Validate inner bounds wound opposite to outer
        for (int boundIndex = 0; boundIndex < bounds.size(); boundIndex++) {
            FaceBound bound = bounds.get(boundIndex);
            if (!bound.outer()) {
                WindingDirection innerWinding = calculateWindingDirection(bound.getLoop());
                if (innerWinding == WindingDirection.UNKNOWN) {
                    continue;
                }

                // Inner bound should wound opposite to outer (considering orientation flag)
                boolean orientationMatches = (outerWinding == innerWinding);
                boolean expectedOrientationMatches = !bound.orientation(); // Inner should be opposite

                if (orientationMatches != expectedOrientationMatches) {
                    String message = bound.orientation()
                            ? "inner bound at face " + faceIndex + " bound " + boundIndex
                              + " winds same as outer but should wind opposite"
                            : "inner bound at face " + faceIndex + " bound " + boundIndex
                              + " winds opposite to outer but orientation flag is reversed";

                    issues.add(MiniCadIssue.error(
                            "face_bound.winding_direction",
                            null,
                            null,
                            message
                    ));
                }
            }
        }
    }

    /**
     * Calculates winding direction of a loop (clockwise or counterclockwise).
     * Uses signed area calculation projected to the best-fit plane.
     *
     * @param loop loop to analyze
     * @return winding direction
     */
    private static WindingDirection calculateWindingDirection(Loop loop) {
        if (loop instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) loop;
            List<CartesianPoint> points = edgeLoop.vertices().stream()
                    .map(Vertex::point)
                    .collect(Collectors.toList());
            return calculateWindingFromPoints(points);
        } else if (loop instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) loop;
            return calculateWindingFromPoints(polyLoop.getPoints());
        } else if (loop instanceof VertexLoop) {
            // Vertex loop has no meaningful winding direction
            return WindingDirection.UNKNOWN;
        }
        return WindingDirection.UNKNOWN;
    }

    /**
     * Calculates winding direction from a list of points.
     * Uses signed area calculation in 2D projection.
     *
     * @param points ordered points of the loop
     * @return winding direction
     */
    private static WindingDirection calculateWindingFromPoints(List<CartesianPoint> points) {
        if (points.size() < 3) {
            return WindingDirection.UNKNOWN;
        }

        // Calculate signed area in 2D projection (use X-Y plane)
        // For more accuracy, should project to the best-fit plane of the points
        double signedArea = 0.0;
        for (int i = 0; i < points.size(); i++) {
            CartesianPoint current = points.get(i);
            CartesianPoint next = points.get((i + 1) % points.size());
            signedArea += (current.getX() * next.getY() - next.getX() * current.getY());
        }

        // Positive signed area = counterclockwise (CCW)
        // Negative signed area = clockwise (CW)
        if (Math.abs(signedArea) < Epsilon.EPS) {
            return WindingDirection.UNKNOWN;
        }

        return signedArea > 0 ? WindingDirection.CounterClockwise : WindingDirection.Clockwise;
    }

    /**
     * Winding direction enumeration.
     */
    private enum WindingDirection {
        Clockwise,
        CounterClockwise,
        UNKNOWN
    }

    // ========================================================================
    // D10: Zero-length edge detection
    // ========================================================================

    /**
     * Detects zero-length edges in face bounds.
     *
     * @param face face to check
     * @param faceIndex face index in shell
     * @param issues list to add validation issues
     */
    private static void detectZeroLengthEdgesInFace(Face face, int faceIndex, List<MiniCadIssue> issues) {
        for (int boundIndex = 0; boundIndex < face.getBounds().size(); boundIndex++) {
            FaceBound bound = face.getBounds().get(boundIndex);
            Loop loop = bound.getLoop();

            if (loop instanceof EdgeLoop) {
                EdgeLoop edgeLoop = (EdgeLoop) loop;
                for (int edgeIndex = 0; edgeIndex < edgeLoop.edges().size(); edgeIndex++) {
                    OrientedEdge orientedEdge = edgeLoop.edges().get(edgeIndex);
                    Edge edge = orientedEdge.getEdge();
                    double length = edge.length();

                    if (length < Epsilon.EPS) {
                        issues.add(MiniCadIssue.error(
                                "edge.zero_length",
                                null,
                                null,
                                "zero-length edge at face " + faceIndex + " bound " + boundIndex
                                        + " edge " + edgeIndex + " (" + describeEdge(edge) + ")"
                        ));
                    }
                }
            } else if (loop instanceof PolyLoop) {
                PolyLoop polyLoop = (PolyLoop) loop;
                List<CartesianPoint> points = polyLoop.getPoints();
                for (int pointIndex = 0; pointIndex < points.size(); pointIndex++) {
                    CartesianPoint p1 = points.get(pointIndex);
                    CartesianPoint p2 = points.get((pointIndex + 1) % points.size());
                    double segmentLength = p1.distanceTo(p2);

                    if (segmentLength < Epsilon.EPS) {
                        issues.add(MiniCadIssue.error(
                                "polyloop.zero_length_segment",
                                null,
                                null,
                                "zero-length segment at face " + faceIndex + " bound " + boundIndex
                                        + " point " + pointIndex + " (" + describePoint(p1) + " -> " + describePoint(p2) + ")"
                        ));
                    }
                }
            }
        }
    }

    private static String describePoint(CartesianPoint point) {
        return String.format(
                "%.6f,%.6f,%.6f",
                point.getX(),
                point.getY(),
                point.getZ()
        );
    }
}
