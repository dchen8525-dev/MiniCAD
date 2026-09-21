package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal measure representation item for native validation payloads.
 *
 * @param id STEP instance id
 * @param name item name
 * @param measureType typed measure wrapper name
 * @param value numeric value
 * @param unit unit reference
 */
public final class StepMeasureRepresentationItem extends AbstractStepEntity {
    private final String measureType;
    private final double value;
    private final StepEntity unit;

    public StepMeasureRepresentationItem(int id, String name, String measureType, double value, StepEntity unit) {
        super(id, name);
        this.measureType = measureType;
        this.value = value;
        this.unit = unit;
    }

    public String getMeasureType() {
        return measureType;
    }

    public double getValue() {
        return value;
    }

    public StepEntity getUnit() {
        return unit;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public String measureType() {
        return measureType;
    }

    public double value() {
        return value;
    }

    public StepEntity unit() {
        return unit;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("measureType", measureType);
        state.put("value", value);
        state.put("unit", unit);
        return state;
    }
}
