package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CONVERSION_BASED_UNIT_AND_UNIT.
 */
public final class StepConversionBasedUnitAndUnit extends AbstractStepEntity {
    private final StepEntity convertedUnit;
    private final StepEntity unitComponent;

    public StepConversionBasedUnitAndUnit(int id, String name, StepEntity convertedUnit, StepEntity unitComponent) {
        super(id, name);
        this.convertedUnit = convertedUnit;
        this.unitComponent = unitComponent;
    }

    public StepEntity getConvertedUnit() {
        return convertedUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("convertedUnit", convertedUnit);
        state.put("unitComponent", unitComponent);
        return state;
    }
}
