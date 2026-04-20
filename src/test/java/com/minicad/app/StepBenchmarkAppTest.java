package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StepBenchmarkAppTest {

    @Test
    void shouldBenchmarkMinimalStepPipeline() {
        StepBenchmarkApp.BenchmarkResult result = StepBenchmarkApp.benchmark("inline", """
                ISO-10303-21;
                HEADER;
                ENDSEC;
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                ENDSEC;
                END-ISO-10303-21;
                """);

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
        StepBenchmarkApp.BenchmarkResult result = StepBenchmarkApp.benchmark("inline", """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                ENDSEC;
                """);

        String output = StepBenchmarkApp.formatResults(List.of(result));

        assertTrue(output.contains("STEP Benchmark Results"));
        assertTrue(output.contains("parseMs:"));
        assertTrue(output.contains("resolveMs:"));
        assertTrue(output.contains("buildMs:"));
        assertTrue(output.contains("previewExportMs:"));
        assertTrue(output.contains("meshExportMs:"));
    }
}
