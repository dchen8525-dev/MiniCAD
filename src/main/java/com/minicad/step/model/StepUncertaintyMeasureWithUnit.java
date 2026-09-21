package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal uncertainty measure with unit.
 *
 * @param id STEP instance id
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 * @param name uncertainty name
 * @param description uncertainty description
 */
public final class StepUncertaintyMeasureWithUnit extends AbstractStepEntity {
    private final double valueComponent;
    private final StepEntity unitComponent;
    private final String description;

    public StepUncertaintyMeasureWithUnit(int id, double valueComponent, StepEntity unitComponent, String name, String description) {
        super(id, name);
        this.valueComponent = valueComponent;
        this.unitComponent = unitComponent;
        this.description = description;
    }

    public double getValueComponent() {
        return valueComponent;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    public String getDescription() {
        return description;
    }

    // Record-style accessors
    public double valueComponent() { return getValueComponent(); }
    public StepEntity unitComponent() { return getUnitComponent(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("valueComponent", valueComponent);
        state.put("unitComponent", unitComponent);
        state.put("name", getName());
        state.put("description", description);
        return state;
    }
}
