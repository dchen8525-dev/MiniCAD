package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepBenchmarkDefinition extends AbstractStepEntity {
    private final String benchmarkType;
    private final List<StepEntity> benchmarkMetrics;
    private final List<Double> benchmarkReference;
    private final String benchmarkDescription;
    private final String benchmarkStatus;

    public StepBenchmarkDefinition(int id, String name, String benchmarkType, List<StepEntity> benchmarkMetrics, List<Double> benchmarkReference, String benchmarkDescription, String benchmarkStatus) {
        super(id, name);
        this.benchmarkType = benchmarkType;
        this.benchmarkMetrics = benchmarkMetrics == null ? null : java.util.List.copyOf(benchmarkMetrics);
        this.benchmarkReference = benchmarkReference == null ? null : java.util.List.copyOf(benchmarkReference);
        this.benchmarkDescription = benchmarkDescription;
        this.benchmarkStatus = benchmarkStatus;
    }

    public String getBenchmarkType() {
        return benchmarkType;
    }

    public List<StepEntity> getBenchmarkMetrics() {
        return benchmarkMetrics;
    }

    public List<Double> getBenchmarkReference() {
        return benchmarkReference;
    }

    public String getBenchmarkDescription() {
        return benchmarkDescription;
    }

    public String getBenchmarkStatus() {
        return benchmarkStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("benchmarkType", benchmarkType);
        state.put("benchmarkMetrics", benchmarkMetrics);
        state.put("benchmarkReference", benchmarkReference);
        state.put("benchmarkDescription", benchmarkDescription);
        state.put("benchmarkStatus", benchmarkStatus);
        return state;
    }
}
