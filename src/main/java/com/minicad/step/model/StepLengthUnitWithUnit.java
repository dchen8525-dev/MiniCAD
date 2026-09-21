package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved LENGTH_UNIT_WITH_UNIT.
 */
public final class StepLengthUnitWithUnit extends AbstractStepEntity {
    private final StepEntity lengthUnit;
    private final StepEntity unitComponent;

    public StepLengthUnitWithUnit(int id, String name, StepEntity lengthUnit, StepEntity unitComponent) {
        super(id, name);
        this.lengthUnit = lengthUnit;
        this.unitComponent = unitComponent;
    }

    public StepEntity getLengthUnit() {
        return lengthUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("lengthUnit", lengthUnit);
        state.put("unitComponent", unitComponent);
        return state;
    }
}
