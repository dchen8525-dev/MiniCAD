package com.minicad.app;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * Minimal CLI demo that reads a STEP file and prints a structural summary.
 */
public final class StepDumpApp {

    private StepDumpApp() {
    }

    /**
     * CLI entry point.
     *
     * @param args command-line arguments
     * @throws IOException if reading the file fails
     */
    public static void main(String[] args) throws IOException {
        int exitCode = run(args, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args, PrintStream out, PrintStream err) throws IOException {
        if (args.length != 1) {
            err.println("Usage: StepDumpApp <step-file>");
            return 2;
        }

        Path path = Path.of(args[0]);
        String text = Files.readString(path);

        try {
            StepFile stepFile = StepParser.parse(text);
            Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
            StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

            out.println("File: " + path);
            out.println();
            printSyntaxSummary(stepFile, out);
            out.println();
            printSemanticSummary(resolved, out);
            out.println();
            printBuildSummary(resolved, builder, out);
            return 0;
        } catch (StepParseException | StepResolutionException | UnsupportedGeometryException ex) {
            err.println("STEP processing failed: " + ex.getMessage());
            return 1;
        }
    }

    private static void printSyntaxSummary(StepFile file, PrintStream out) {
        out.println("Syntax Summary");
        out.println("  entityCount: " + file.entities().size());
        if (!file.entities().isEmpty()) {
            out.println("  firstId: #" + file.entities().getFirst().id());
            out.println("  lastId: #" + file.entities().getLast().id());
        }
    }

    private static void printSemanticSummary(Map<Integer, StepEntity> resolved, PrintStream out) {
        out.println("Semantic Summary");
        Map<String, Integer> counts = new TreeMap<>();
        for (StepEntity entity : resolved.values()) {
            counts.merge(entity.getClass().getSimpleName(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void printBuildSummary(Map<Integer, StepEntity> resolved, StepCadBuilder builder, PrintStream out) {
        out.println("Build Summary");

        int openShells = 0;
        int closedShells = 0;
        int solids = 0;

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell openShell) {
                Shell shell = builder.buildShell(openShell.id());
                out.println("  openShell #" + openShell.id() + ": faces=" + shell.faces().size());
                openShells++;
            } else if (entity instanceof StepClosedShell closedShell) {
                Shell shell = builder.buildShell(closedShell.id());
                out.println("  closedShell #" + closedShell.id() + ": faces=" + shell.faces().size());
                closedShells++;
            } else if (entity instanceof StepManifoldSolidBrep solidBrep) {
                Solid solid = builder.buildSolid(solidBrep.id());
                out.println("  solid #" + solidBrep.id() + ": shellFaces=" + solid.outerShell().faces().size());
                solids++;
            }
        }

        out.println("  totals: openShells=" + openShells + ", closedShells=" + closedShells + ", solids=" + solids);
    }
}
