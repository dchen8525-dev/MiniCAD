package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StepBenchmarkAppTest {

    @Test
    void shouldBenchmarkMinimalStepPipeline() {
        StepBenchmarkApp.BenchmarkResult result = StepBenchmarkApp.benchmark("inline",
        "ISO-10303-21;\n"
        + "HEADER;\n"
        + "ENDSEC;\n"
        + "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "ENDSEC;\n"
        + "END-ISO-10303-21;\n"
        );

        assertTrue(result.entityCount() >= 2);
        assertTrue(result.resolvedCount() >= 2);
        assertTrue(result.parseElapsedNanos() >= 0);
        assertTrue(result.resolveElapsedNanos() >= 0);
        assertTrue(result.buildElapsedNanos() >= 0);
        assertTrue(result.previewExportElapsedNanos() >= 0);
        assertTrue(result.meshExportElapsedNanos() >= 0);
    }

    @Test
    void shouldFormatBenchmarkResults() {
        StepBenchmarkApp.BenchmarkResult result = StepBenchmarkApp.benchmark("inline",
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "ENDSEC;\n"
        );

        String output = StepBenchmarkApp.formatResults(List.of(result));

        assertTrue(output.contains("STEP Benchmark Results"));
        assertTrue(output.contains("parseMs:"));
        assertTrue(output.contains("resolveMs:"));
        assertTrue(output.contains("buildMs:"));
        assertTrue(output.contains("previewExportMs:"));
        assertTrue(output.contains("meshExportMs:"));
    }

    @Test
    void shouldFormatFirstBuildFailureReasons() {
        StepBenchmarkApp.BuildSummary summary = new StepBenchmarkApp.BuildSummary(
                1,
                1,
                2,
                1,
                3,
                1,
                "#10 StepAdvancedFace: unsupported surface",
                "#20 StepGeometricSurfaceSet: requires bounded face geometry",
                "#30 StepManifoldSolidBrep: closed shell edge use count"
        );
        StepBenchmarkApp.BenchmarkResult result = new StepBenchmarkApp.BenchmarkResult(
                "inline",
                100,
                100,
                3,
                3,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                summary,
                10,
                20
        );

        String output = StepBenchmarkApp.formatResults(List.of(result));

        assertTrue(output.contains("firstFaceBuildFailure: #10 StepAdvancedFace: unsupported surface"));
        assertTrue(output.contains("firstShellBuildFailure: #20 StepGeometricSurfaceSet: requires bounded face geometry"));
        assertTrue(output.contains("firstSolidBuildFailure: #30 StepManifoldSolidBrep: closed shell edge use count"));
    }
}
