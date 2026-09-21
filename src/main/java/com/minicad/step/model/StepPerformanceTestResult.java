package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PERFORMANCE_TEST_RESULT.
 * A performance test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceSystem tested variance system
 * @varianceMetrics performance variance metrics
 * @varianceValues measured variance values
 * @varianceBaseline baseline variance reference
 * @varianceCondition condition variance met flag
 * @varianceStatus result variance status
 */
public final class StepPerformanceTestResult extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final List<String> varianceMetrics;
    private final List<Double> varianceValues;
    private final StepEntity varianceBaseline;
    private final boolean varianceCondition;
    private final String varianceStatus;

    public StepPerformanceTestResult(int id, String name, StepEntity varianceSystem, List<String> varianceMetrics, List<Double> varianceValues, StepEntity varianceBaseline, boolean varianceCondition, String varianceStatus) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceValues = varianceValues == null ? null : java.util.List.copyOf(varianceValues);
        this.varianceBaseline = varianceBaseline;
        this.varianceCondition = varianceCondition;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public List<String> getVarianceMetrics() {
        return varianceMetrics;
    }

    public List<Double> getVarianceValues() {
        return varianceValues;
    }

    public StepEntity getVarianceBaseline() {
        return varianceBaseline;
    }

    public boolean isVarianceCondition() {
        return varianceCondition;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceSystem", varianceSystem);
        state.put("varianceMetrics", varianceMetrics);
        state.put("varianceValues", varianceValues);
        state.put("varianceBaseline", varianceBaseline);
        state.put("varianceCondition", varianceCondition);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
