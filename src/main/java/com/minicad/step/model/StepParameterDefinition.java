package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PARAMETER_DEFINITION.
 * A parameter definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceParameter defined variance parameter
 * @varianceType parameter variance type
 * @varianceDefaultValue default variance value
 * @varianceRange allowed variance range
 * @varianceUnit parameter variance unit
 * @varianceStatus definition variance status
 */
public final class StepParameterDefinition extends AbstractStepEntity {
    private final String varianceParameter;
    private final String varianceType;
    private final double varianceDefaultValue;
    private final List<Double> varianceRange;
    private final StepEntity varianceUnit;
    private final String varianceStatus;

    public StepParameterDefinition(int id, String name, String varianceParameter, String varianceType, double varianceDefaultValue, List<Double> varianceRange, StepEntity varianceUnit, String varianceStatus) {
        super(id, name);
        this.varianceParameter = varianceParameter;
        this.varianceType = varianceType;
        this.varianceDefaultValue = varianceDefaultValue;
        this.varianceRange = varianceRange == null ? null : java.util.List.copyOf(varianceRange);
        this.varianceUnit = varianceUnit;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceParameter() {
        return varianceParameter;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public double getVarianceDefaultValue() {
        return varianceDefaultValue;
    }

    public List<Double> getVarianceRange() {
        return varianceRange;
    }

    public StepEntity getVarianceUnit() {
        return varianceUnit;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceParameter", varianceParameter);
        state.put("varianceType", varianceType);
        state.put("varianceDefaultValue", varianceDefaultValue);
        state.put("varianceRange", varianceRange);
        state.put("varianceUnit", varianceUnit);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
