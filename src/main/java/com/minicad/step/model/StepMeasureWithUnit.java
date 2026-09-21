package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal measure-with-unit value.
 *
 * @param id STEP instance id
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 */
public final class StepMeasureWithUnit extends AbstractStepEntity {
    private final double valueComponent;
    private final StepEntity unitComponent;

    public StepMeasureWithUnit(int id, double valueComponent, StepEntity unitComponent) {
        super(id, "");
        this.valueComponent = valueComponent;
        this.unitComponent = unitComponent;
    }

    public double getValueComponent() {
        return valueComponent;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public double valueComponent() { return valueComponent; }
    public StepEntity unitComponent() { return unitComponent; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("valueComponent", valueComponent);
        state.put("unitComponent", unitComponent);
        return state;
    }
}
