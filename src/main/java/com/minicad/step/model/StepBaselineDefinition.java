package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved BASELINE_DEFINITION.
 * A baseline definition entity.
 *
 * @param id STEP instance id
 * @param name baseline name
 * @param baselineType baseline variance type
 * @param baselineValues baseline variance values
 * @param baselineUnit baseline variance unit
 * @param baselineDescription baseline variance description
 * @param baselineStatus baseline variance status
 */
public final class StepBaselineDefinition extends AbstractStepEntity {
    private final String baselineType;
    private final List<Double> baselineValues;
    private final StepEntity baselineUnit;
    private final String baselineDescription;
    private final String baselineStatus;

    public StepBaselineDefinition(int id, String name, String baselineType, List<Double> baselineValues, StepEntity baselineUnit, String baselineDescription, String baselineStatus) {
        super(id, name);
        this.baselineType = baselineType;
        this.baselineValues = baselineValues == null ? null : java.util.List.copyOf(baselineValues);
        this.baselineUnit = baselineUnit;
        this.baselineDescription = baselineDescription;
        this.baselineStatus = baselineStatus;
    }

    public String getBaselineType() {
        return baselineType;
    }

    public List<Double> getBaselineValues() {
        return baselineValues;
    }

    public StepEntity getBaselineUnit() {
        return baselineUnit;
    }

    public String getBaselineDescription() {
        return baselineDescription;
    }

    public String getBaselineStatus() {
        return baselineStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("baselineType", baselineType);
        state.put("baselineValues", baselineValues);
        state.put("baselineUnit", baselineUnit);
        state.put("baselineDescription", baselineDescription);
        state.put("baselineStatus", baselineStatus);
        return state;
    }
}
