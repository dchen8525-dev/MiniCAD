package com.minicad.app;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Diagnostic entry point for measuring STEP pipeline stage timings.
 */
public final class StepBenchmarkApp {

    private StepBenchmarkApp() {
    }

    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: StepBenchmarkApp <step-file> [<step-file> ...]");
            return;
        }

        List<BenchmarkResult> results = new ArrayList<>();
        for (String arg : args) {
            Path path = Path.of(arg);
            long readStartedAt = System.nanoTime();
            byte[] bytes = java.nio.file.Files.readAllBytes(path);
            StepTextReader.DecodedStepText decoded = StepTextReader.readDecoded(bytes);
            long readElapsedNanos = System.nanoTime() - readStartedAt;
            results.add(benchmark(path.toString(), decoded.text(), bytes.length, readElapsedNanos));
        }

        System.out.print(formatResults(results));
    }

    static BenchmarkResult benchmark(String label, String stepText) {
        return benchmark(label, stepText, stepText.getBytes(StandardCharsets.UTF_8).length, 0L);
    }

    static BenchmarkResult benchmark(String label, String stepText, long inputBytes, long readElapsedNanos) {
        long parseStartedAt = System.nanoTime();
        StepFile stepFile = StepParser.parse(stepText);
        long parseElapsedNanos = System.nanoTime() - parseStartedAt;

        long resolveStartedAt = System.nanoTime();
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        long resolveElapsedNanos = System.nanoTime() - resolveStartedAt;

        long buildStartedAt = System.nanoTime();
        BuildSummary buildSummary = benchmarkBuild(resolved);
        long buildElapsedNanos = System.nanoTime() - buildStartedAt;

        long previewExportStartedAt = System.nanoTime();
        String previewJson = StepPreviewJsonExporter.export(stepText);
        long previewExportElapsedNanos = System.nanoTime() - previewExportStartedAt;

        long meshExportStartedAt = System.nanoTime();
        String meshObj = StepMeshExporter.exportObj(stepText);
        long meshExportElapsedNanos = System.nanoTime() - meshExportStartedAt;

        return new BenchmarkResult(
                label,
                inputBytes,
                stepText.length(),
                stepFile.entities().size(),
                resolved.size(),
                readElapsedNanos,
                parseElapsedNanos,
                resolveElapsedNanos,
                buildElapsedNanos,
                previewExportElapsedNanos,
                meshExportElapsedNanos,
                usedMemoryBytes(),
                buildSummary,
                previewJson.length(),
                meshObj.length()
        );
    }

    static String formatResults(List<BenchmarkResult> results) {
        StringBuilder out = new StringBuilder();
        out.append("STEP Benchmark Results\n");
        for (BenchmarkResult result : results) {
            out.append("\n");
            out.append("file: ").append(result.label()).append("\n");
            out.append("inputBytes: ").append(result.inputBytes()).append("\n");
            out.append("textLength: ").append(result.textLength()).append("\n");
            out.append("entityCount: ").append(result.entityCount()).append("\n");
            out.append("resolvedCount: ").append(result.resolvedCount()).append("\n");
            out.append("readMs: ").append(formatMillis(result.readElapsedNanos())).append("\n");
            out.append("parseMs: ").append(formatMillis(result.parseElapsedNanos())).append("\n");
            out.append("resolveMs: ").append(formatMillis(result.resolveElapsedNanos())).append("\n");
            out.append("buildMs: ").append(formatMillis(result.buildElapsedNanos())).append("\n");
            out.append("previewExportMs: ").append(formatMillis(result.previewExportElapsedNanos())).append("\n");
            out.append("meshExportMs: ").append(formatMillis(result.meshExportElapsedNanos())).append("\n");
            out.append("approxUsedMemoryBytes: ").append(result.approxUsedMemoryBytes()).append("\n");
            out.append("buildFacesOk: ").append(result.buildSummary().facesBuilt()).append("\n");
            out.append("buildFacesFailed: ").append(result.buildSummary().faceBuildFailures()).append("\n");
            out.append("buildShellsOk: ").append(result.buildSummary().shellsBuilt()).append("\n");
            out.append("buildShellsFailed: ").append(result.buildSummary().shellBuildFailures()).append("\n");
            out.append("buildSolidsOk: ").append(result.buildSummary().solidsBuilt()).append("\n");
            out.append("buildSolidsFailed: ").append(result.buildSummary().solidBuildFailures()).append("\n");
            out.append("previewJsonLength: ").append(result.previewJsonLength()).append("\n");
            out.append("meshObjLength: ").append(result.meshObjLength()).append("\n");
        }
        return out.toString();
    }

    private static BuildSummary benchmarkBuild(Map<Integer, StepEntity> resolved) {
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);
        int facesBuilt = 0;
        int faceBuildFailures = 0;
        int shellsBuilt = 0;
        int shellBuildFailures = 0;
        int solidsBuilt = 0;
        int solidBuildFailures = 0;

        for (Map.Entry<Integer, StepEntity> entry : resolved.entrySet()) {
            int id = entry.getKey();
            StepEntity entity = entry.getValue();
            if (entity instanceof com.minicad.step.model.base.StepFaceEntity) {
                try {
                    builder.buildFace(id);
                    facesBuilt++;
                } catch (Exception ignored) {
                    faceBuildFailures++;
                }
                continue;
            }
            if (builder.canBuildAsSolid(entity)) {
                try {
                    builder.buildSolid(id);
                    solidsBuilt++;
                } catch (Exception ignored) {
                    solidBuildFailures++;
                }
                continue;
            }
            if (isShellCandidate(entity)) {
                try {
                    builder.buildShell(id);
                    shellsBuilt++;
                } catch (Exception ignored) {
                    shellBuildFailures++;
                }
            }
        }

        return new BuildSummary(
                facesBuilt,
                faceBuildFailures,
                shellsBuilt,
                shellBuildFailures,
                solidsBuilt,
                solidBuildFailures
        );
    }

    private static boolean isShellCandidate(StepEntity entity) {
        return entity instanceof com.minicad.step.model.topology.StepOpenShell
                || entity instanceof com.minicad.step.model.topology.StepClosedShell
                || entity instanceof com.minicad.step.model.geometry.StepSurfacedOpenShell
                || entity instanceof com.minicad.step.model.topology.StepConnectedFaceSet
                || entity instanceof com.minicad.step.model.product.StepTessellatedFaceSet
                || entity instanceof com.minicad.step.model.product.StepTessellatedFace
                || entity instanceof com.minicad.step.model.product.StepFaceBasedSurfaceModel
                || entity instanceof com.minicad.step.model.geometry.StepManifoldSurfaceModel
                || entity instanceof com.minicad.step.model.product.StepShellBasedSurfaceModel;
    }

    private static double formatMillis(long nanos) {
        return nanos / 1_000_000.0;
    }

    private static long usedMemoryBytes() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    record BuildSummary(
            int facesBuilt,
            int faceBuildFailures,
            int shellsBuilt,
            int shellBuildFailures,
            int solidsBuilt,
            int solidBuildFailures
    ) {
    }

    record BenchmarkResult(
            String label,
            long inputBytes,
            int textLength,
            int entityCount,
            int resolvedCount,
            long readElapsedNanos,
            long parseElapsedNanos,
            long resolveElapsedNanos,
            long buildElapsedNanos,
            long previewExportElapsedNanos,
            long meshExportElapsedNanos,
            long approxUsedMemoryBytes,
            BuildSummary buildSummary,
            int previewJsonLength,
            int meshObjLength
    ) {
    }
}
