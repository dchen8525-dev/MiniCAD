package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BENCHMARK_DEFINITION.
 * A benchmark definition entity.
 *
 * @param id STEP instance id
 * @param name benchmark name
 * @param benchmarkType benchmark variance type
 * @param benchmarkMetrics benchmark variance metrics
 * @param benchmarkReference benchmark variance reference values
 * @param benchmarkDescription benchmark variance description
 * @param benchmarkStatus benchmark variance status
 */
public record StepBenchmarkDefinition(
    int id,
    String name,
    String benchmarkType,
    List<StepEntity> benchmarkMetrics,
    List<Double> benchmarkReference,
    String benchmarkDescription,
    String benchmarkStatus) implements StepEntity {
}