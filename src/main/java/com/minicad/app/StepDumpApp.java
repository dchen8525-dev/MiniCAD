package com.minicad.app;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.common.GeometryException;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

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
        int unsupportedFaces = 0;

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell openShell) {
                FaceBuildCounts counts = summarizeShell(openShell.faces(), builder);
                lines.add("  openShell #" + openShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
            } else if (entity instanceof StepClosedShell closedShell) {
                FaceBuildCounts counts = summarizeShell(closedShell.faces(), builder);
                lines.add("  closedShell #" + closedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
            } else if (entity instanceof StepManifoldSolidBrep solidBrep) {
                FaceBuildCounts counts = summarizeShell(solidBrep.outer().faces(), builder);
                lines.add("  solid #" + solidBrep.id() + ": shellFaces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                solids++;
            }
        }

        lines.add("  totals: openShells=" + openShells + ", closedShells=" + closedShells
                + ", solids=" + solids + ", unsupportedFaces=" + unsupportedFaces);
    }

    private static FaceBuildCounts summarizeShell(Iterable<StepFaceEntity> faces, StepCadBuilder builder) {
        int supported = 0;
        int unsupported = 0;
        for (StepFaceEntity face : faces) {
            try {
                builder.buildFace(face.id());
                supported++;
            } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                unsupported++;
            }
        }
        return new FaceBuildCounts(supported, unsupported);
    }

    private record FaceBuildCounts(int supportedFaces, int unsupportedFaces) {
    }
}
