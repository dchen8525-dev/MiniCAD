package com.minicad.app;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.common.GeometryException;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Minimal CLI demo that reads a STEP file and prints a structural summary.
 */
public final class StepDumpApp {
    private static final Logger log = LoggerFactory.getLogger(StepDumpApp.class);

    private StepDumpApp() {
    }

    /**
     * CLI entry point.
     *
     * @param args command-line arguments
     * @throws IOException if reading the file fails
     */
    public static void main(String[] args) throws IOException {
        int exitCode = run(args, log::info, log::error);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args, Consumer<String> out, Consumer<String> err) throws IOException {
        if (args.length != 1) {
            err.accept("Usage: StepDumpApp <step-file>");
            return 2;
        }

        Path path = Path.of(args[0]);
        String text = StepTextReader.read(path);

        try {
            StepFile stepFile = StepParser.parse(text);
            Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
            StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

            List<String> lines = new ArrayList<>();
            lines.add("File: " + path);
            lines.add("");
            appendSyntaxSummary(stepFile, lines);
            lines.add("");
            appendSemanticSummary(resolved, lines);
            lines.add("");
            appendBuildSummary(resolved, builder, lines);
            lines.forEach(out);
            return 0;
        } catch (StepParseException | StepResolutionException | UnsupportedGeometryException | TopologyException | GeometryException ex) {
            err.accept("STEP processing failed: " + ex.getMessage());
            return 1;
        }
    }

    private static void appendSyntaxSummary(StepFile file, List<String> lines) {
        lines.add("Syntax Summary");
        lines.add("  entityCount: " + file.entities().size());
        if (!file.entities().isEmpty()) {
            lines.add("  firstId: #" + file.entities().getFirst().id());
            lines.add("  lastId: #" + file.entities().getLast().id());
        }
    }

    private static void appendSemanticSummary(Map<Integer, StepEntity> resolved, List<String> lines) {
        lines.add("Semantic Summary");
        Map<String, Integer> counts = new TreeMap<>();
        for (StepEntity entity : resolved.values()) {
            counts.merge(entity.getClass().getSimpleName(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            lines.add("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void appendBuildSummary(Map<Integer, StepEntity> resolved, StepCadBuilder builder, List<String> lines) {
        lines.add("Build Summary");

        int openShells = 0;
        int closedShells = 0;
        int solids = 0;
        int booleanResults = 0;
        int unsupportedFaces = 0;
        Map<String, Integer> unsupportedReasons = new LinkedHashMap<>();
        Map<String, Integer> unsupportedReasonCodes = new LinkedHashMap<>();

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell openShell) {
                FaceBuildCounts counts = summarizeShell(openShell.faces(), builder);
                lines.add("  openShell #" + openShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
                FaceBuildCounts counts = summarizeShell(surfacedOpenShell.faces(), builder);
                lines.add("  surfacedOpenShell #" + surfacedOpenShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepClosedShell closedShell) {
                FaceBuildCounts counts = summarizeShell(closedShell.faces(), builder);
                lines.add("  closedShell #" + closedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
                FaceBuildCounts counts = summarizeShell(orientedOpenShell.faces(), builder);
                lines.add("  orientedOpenShell #" + orientedOpenShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
                FaceBuildCounts counts = summarizeShell(orientedClosedShell.faces(), builder);
                lines.add("  orientedClosedShell #" + orientedClosedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepManifoldSolidBrep solidBrep) {
                FaceBuildCounts counts = summarizeShell(shellFaces(solidBrep.outer()), builder);
                lines.add("  solid #" + solidBrep.id() + ": shellFaces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                solids++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepBooleanClippingResult clippingResult) {
                Map<String, Integer> reasonCounts = Map.of("BOOLEAN_CLIPPING_RESULT construction is unsupported", 1);
                Map<String, Integer> reasonCodeCounts = Map.of("unsupported_boolean.clipping_result", 1);
                lines.add("  booleanClippingResult #" + clippingResult.id() + ": faces=0, unsupportedFaces=1");
                appendUnsupportedReasons(lines, reasonCounts);
                appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                booleanResults++;
                unsupportedFaces++;
                mergeReasonCounts(unsupportedReasons, reasonCounts);
                mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
            } else if (entity instanceof StepBooleanResult booleanResult) {
                Map<String, Integer> reasonCounts = Map.of("BOOLEAN_RESULT construction is unsupported", 1);
                Map<String, Integer> reasonCodeCounts = Map.of("unsupported_boolean.result", 1);
                lines.add("  booleanResult #" + booleanResult.id() + ": faces=0, unsupportedFaces=1");
                appendUnsupportedReasons(lines, reasonCounts);
                appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                booleanResults++;
                unsupportedFaces++;
                mergeReasonCounts(unsupportedReasons, reasonCounts);
                mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
            }
        }

        lines.add("  totals: openShells=" + openShells + ", closedShells=" + closedShells
                + ", solids=" + solids + ", booleanResults=" + booleanResults + ", unsupportedFaces=" + unsupportedFaces);
        appendUnsupportedReasons(lines, unsupportedReasons);
        appendUnsupportedReasonCodes(lines, unsupportedReasonCodes);
    }

    private static Iterable<StepFaceEntity> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell openShell) {
            return openShell.faces();
        }
        if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
            return surfacedOpenShell.faces();
        }
        if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
            return orientedOpenShell.faces();
        }
        if (entity instanceof StepClosedShell closedShell) {
            return closedShell.faces();
        }
        if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
            return orientedClosedShell.faces();
        }
        throw new StepResolutionException("entity #" + entity.id() + " is not a supported shell");
    }

    private static FaceBuildCounts summarizeShell(Iterable<StepFaceEntity> faces, StepCadBuilder builder) {
        int supported = 0;
        int unsupported = 0;
        Map<String, Integer> unsupportedReasons = new LinkedHashMap<>();
        Map<String, Integer> unsupportedReasonCodes = new LinkedHashMap<>();
        for (StepFaceEntity face : faces) {
            try {
                builder.buildFace(face.id());
                supported++;
            } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                unsupported++;
                String reason = normalizeReason(ex.getMessage());
                unsupportedReasons.merge(reason, 1, Integer::sum);
                unsupportedReasonCodes.merge(classifyReasonCode(ex, reason), 1, Integer::sum);
            }
        }
        return new FaceBuildCounts(
                supported,
                unsupported,
                Map.copyOf(unsupportedReasons),
                Map.copyOf(unsupportedReasonCodes)
        );
    }

    private static void appendUnsupportedReasons(List<String> lines, Map<String, Integer> unsupportedReasons) {
        if (unsupportedReasons.isEmpty()) {
            return;
        }
        lines.add("    unsupportedReasons: " + formatReasonCounts(unsupportedReasons));
    }

    private static void appendUnsupportedReasonCodes(List<String> lines, Map<String, Integer> unsupportedReasonCodes) {
        if (unsupportedReasonCodes.isEmpty()) {
            return;
        }
        lines.add("    unsupportedReasonCodes: " + formatReasonCounts(unsupportedReasonCodes));
    }

    private static void mergeReasonCounts(Map<String, Integer> target, Map<String, Integer> source) {
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            target.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    private static String formatReasonCounts(Map<String, Integer> reasonCounts) {
        return reasonCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    private static String normalizeReason(String message) {
        if (message == null || message.isBlank()) {
            return "unknown";
        }
        return message.replace(System.lineSeparator(), " ").trim();
    }

    private static String classifyReasonCode(Exception ex, String reason) {
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
        if (reason.contains("construction for SURFACE_REPLICA is unsupported")) {
            return "unsupported_surface.replica";
        }
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
        if (reason.contains("construction for SURFACE_OF_LINEAR_EXTRUSION is unsupported")) {
            return "unsupported_surface.linear_extrusion";
        }
        if (reason.contains("construction for SURFACE_OF_REVOLUTION is unsupported")) {
            return "unsupported_surface.revolution";
        }
        if (reason.contains("construction for SPHERICAL_SURFACE is unsupported")) {
            return "unsupported_surface.spherical";
        }
        if (reason.contains("BOOLEAN_RESULT construction is unsupported")) {
            return "unsupported_boolean.result";
        }
        if (reason.contains("BOOLEAN_CLIPPING_RESULT construction is unsupported")) {
            return "unsupported_boolean.clipping_result";
        }
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
        if (ex instanceof UnsupportedGeometryException) {
            return "unsupported_geometry.other";
        }
        if (ex instanceof TopologyException) {
            return "topology.other";
        }
        if (ex instanceof StepResolutionException) {
            return "resolution.other";
        }
        if (ex instanceof GeometryException) {
            return "geometry.other";
        }
        return "unknown";
    }

    private record FaceBuildCounts(
            int supportedFaces,
            int unsupportedFaces,
            Map<String, Integer> unsupportedReasons,
            Map<String, Integer> unsupportedReasonCodes
    ) {
    }
}
