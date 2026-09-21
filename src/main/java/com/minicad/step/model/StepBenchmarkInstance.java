package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepBenchmarkInstance extends AbstractStepEntity {
    private final StepEntity benchmarkDefinition;
    private final List<Double> benchmarkValues;
    private final double benchmarkScore;
    private final String benchmarkStatus;

    public StepBenchmarkInstance(int id, String name, StepEntity benchmarkDefinition, List<Double> benchmarkValues, double benchmarkScore, String benchmarkStatus) {
        super(id, name);
        this.benchmarkDefinition = benchmarkDefinition;
        this.benchmarkValues = benchmarkValues == null ? null : java.util.List.copyOf(benchmarkValues);
        this.benchmarkScore = benchmarkScore;
        this.benchmarkStatus = benchmarkStatus;
    }

    public StepEntity getBenchmarkDefinition() {
        return benchmarkDefinition;
    }

    public List<Double> getBenchmarkValues() {
        return benchmarkValues;
    }

    public double getBenchmarkScore() {
        return benchmarkScore;
    }

    public String getBenchmarkStatus() {
        return benchmarkStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("benchmarkDefinition", benchmarkDefinition);
        state.put("benchmarkValues", benchmarkValues);
        state.put("benchmarkScore", benchmarkScore);
        state.put("benchmarkStatus", benchmarkStatus);
        return state;
    }
}
