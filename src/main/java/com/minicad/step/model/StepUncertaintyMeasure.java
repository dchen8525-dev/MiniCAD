package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved UNCERTAINTY_MEASURE.
 * An uncertainty measure with unit.
 */
public final class StepUncertaintyMeasure extends AbstractStepEntity {
    private final double value;
    private final String unit;

    public StepUncertaintyMeasure(int id, String name, double value, String unit) {
        super(id, name);
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("value", value);
        state.put("unit", unit);
        return state;
    }
}
