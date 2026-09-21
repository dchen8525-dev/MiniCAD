package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved BASELINE_INSTANCE.
 * A baseline instance entity.
 *
 * @param id STEP instance id
 * @param name baseline instance name
 * @param baselineDefinition baseline variance definition reference
 * @param baselineActualValues baseline variance actual values
 * @param baselineVariance baseline variance deviation from baseline
 * @param baselineStatus baseline variance status
 */
public final class StepBaselineInstance extends AbstractStepEntity {
    private final StepEntity baselineDefinition;
    private final List<Double> baselineActualValues;
    private final double baselineVariance;
    private final String baselineStatus;

    public StepBaselineInstance(int id, String name, StepEntity baselineDefinition, List<Double> baselineActualValues, double baselineVariance, String baselineStatus) {
        super(id, name);
        this.baselineDefinition = baselineDefinition;
        this.baselineActualValues = baselineActualValues == null ? null : java.util.List.copyOf(baselineActualValues);
        this.baselineVariance = baselineVariance;
        this.baselineStatus = baselineStatus;
    }

    public StepEntity getBaselineDefinition() {
        return baselineDefinition;
    }

    public List<Double> getBaselineActualValues() {
        return baselineActualValues;
    }

    public double getBaselineVariance() {
        return baselineVariance;
    }

    public String getBaselineStatus() {
        return baselineStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("baselineDefinition", baselineDefinition);
        state.put("baselineActualValues", baselineActualValues);
        state.put("baselineVariance", baselineVariance);
        state.put("baselineStatus", baselineStatus);
        return state;
    }
}
