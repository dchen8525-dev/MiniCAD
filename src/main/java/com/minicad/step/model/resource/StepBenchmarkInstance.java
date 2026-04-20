package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved BENCHMARK_INSTANCE.
 * A benchmark instance entity.
 *
 * @param id STEP instance id
 * @param name benchmark instance name
 * @param benchmarkDefinition benchmark variance definition reference
 * @param benchmarkValues benchmark variance measured values
 * @param benchmarkScore benchmark variance score/rating
 * @param benchmarkStatus benchmark variance status
 */
public record StepBenchmarkInstance(
    int id,
    String name,
    StepEntity benchmarkDefinition,
    List<Double> benchmarkValues,
    double benchmarkScore,
    String benchmarkStatus) implements StepEntity {
}