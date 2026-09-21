package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VALIDATION_RESULT.
 * A validation result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem validated variance item
 * @varianceMethod validation variance method
 * @varianceEnvironment validation variance environment
 * @varianceOutcome validation variance outcome
 * @varianceDeficiencies deficiencies variance identified
 * @varianceStatus result variance status
 */
public final class StepValidationResult extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final String varianceMethod;
    private final String varianceEnvironment;
    private final String varianceOutcome;
    private final List<String> varianceDeficiencies;
    private final String varianceStatus;

    public StepValidationResult(int id, String name, StepEntity varianceItem, String varianceMethod, String varianceEnvironment, String varianceOutcome, List<String> varianceDeficiencies, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceMethod = varianceMethod;
        this.varianceEnvironment = varianceEnvironment;
        this.varianceOutcome = varianceOutcome;
        this.varianceDeficiencies = varianceDeficiencies == null ? null : java.util.List.copyOf(varianceDeficiencies);
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public String getVarianceEnvironment() {
        return varianceEnvironment;
    }

    public String getVarianceOutcome() {
        return varianceOutcome;
    }

    public List<String> getVarianceDeficiencies() {
        return varianceDeficiencies;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceMethod", varianceMethod);
        state.put("varianceEnvironment", varianceEnvironment);
        state.put("varianceOutcome", varianceOutcome);
        state.put("varianceDeficiencies", varianceDeficiencies);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
