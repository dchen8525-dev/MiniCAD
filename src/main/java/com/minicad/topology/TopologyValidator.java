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
        for (int faceIndex = 0; faceIndex < shell.faces().size(); faceIndex++) {
            Face face = shell.faces().get(faceIndex);
            validateFace(face, faceIndex, issues);
            for (FaceBound bound : face.bounds()) {
                if (bound.loop() instanceof EdgeLoop) {
                    EdgeLoop edgeLoop = (EdgeLoop) bound.loop();
                    for (OrientedEdge orientedEdge : edgeLoop.edges()) {
                        edgeUses.computeIfAbsent(orientedEdge.edge(), edge -> new EdgeUseSummary())
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
            if (shell.closed() && summary.total() != 2) {
                issues.add(MiniCadIssue.error(
                        "closed_shell.edge_use_count",
                        null,
                        null,
                        "closed shell edge " + describeEdge(edge) + " is used " + summary.total()
                                + " time(s), expected 2"
                ));
            }
            if (shell.closed() && summary.total() == 2
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
        issues.addAll(validateShell(solid.outerShell()).issues());

        BoundingBox3 outerBox = solid.outerShell().boundingBox();
        double outerVolumeSign = Math.signum(signedShellVolume(solid.outerShell()));
        for (int index = 0; index < solid.voidShells().size(); index++) {
            Shell voidShell = solid.voidShells().get(index);
            issues.addAll(validateShell(voidShell).issues());
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
        if (face.surface() instanceof Plane) {
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
    }

    private static double planarOuterArea(Face face) {
        FaceBound outer = face.outerBound();
        if (outer == null) {
            return 0.0;
        }
        if (outer.loop() instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) outer.loop();
            return polygonArea3d(edgeLoop.vertices().stream()
                    .map(Vertex::point)
                    .collect(Collectors.toList()));
        }
        if (outer.loop() instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) outer.loop();
            return polygonArea3d(polyLoop.points());
        }
        return 0.0;
    }

    private static String describeEdge(Edge edge) {
        return "(" + describeVertex(edge.start()) + " -> " + describeVertex(edge.end()) + ")";
    }

    private static String describeVertex(Vertex vertex) {
        return String.format(
                "%.6f,%.6f,%.6f",
                vertex.point().x(),
                vertex.point().y(),
                vertex.point().z()
        );
    }

    private static double signedShellVolume(Shell shell) {
        double volume = 0.0;
        for (Face face : shell.faces()) {
            FaceBound outer = face.outerBound();
            if (outer != null && outer.loop() instanceof EdgeLoop) { EdgeLoop edgeLoop = (EdgeLoop) outer.loop();
                double contribution = signedLoopVolume(edgeLoop.vertices());
                volume += face.sameSense() ? contribution : -contribution;
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
            x += cross.x();
            y += cross.y();
            z += cross.z();
        }
        return 0.5 * Math.sqrt(x * x + y * y + z * z);
    }

    private static double dot(CartesianPoint a, CartesianPoint b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }

    private static CartesianPoint cross(CartesianPoint a, CartesianPoint b) {
        return new CartesianPoint(
                a.y() * b.z() - a.z() * b.y(),
                a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x()
        );
    }

    private static final class EdgeUseSummary {
        private int forwardCount;
        private int reverseCount;

        private void add(OrientedEdge edge) {
            if (edge.orientation()) {
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
}
