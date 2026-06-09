package com.minicad.topology;

import com.minicad.common.MiniCadIssue;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

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
        for (Face face : shell.faces()) {
            for (FaceBound bound : face.bounds()) {
                if (bound.loop() instanceof EdgeLoop edgeLoop) {
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
    public record ValidationResult(List<MiniCadIssue> issues) {

        public ValidationResult {
            issues = List.copyOf(issues);
        }

        /**
         * Returns whether no validation issues were found.
         *
         * @return true when valid
         */
        public boolean ok() {
            return issues.isEmpty();
        }

        /**
         * Returns the number of error issues.
         *
         * @return error count
         */
        public long errorCount() {
            return issues.stream()
                    .filter(issue -> issue.severity() == MiniCadIssue.Severity.ERROR)
                    .count();
        }

        /**
         * Returns whether a code is present.
         *
         * @param code issue code
         * @return true if any issue has the code
         */
        public boolean hasCode(String code) {
            return issues.stream().anyMatch(issue -> issue.code().equals(code));
        }
    }
}
