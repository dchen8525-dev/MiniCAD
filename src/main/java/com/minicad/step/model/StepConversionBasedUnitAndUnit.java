package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "convertedUnit", convertedUnit,
                "unitComponent", unitComponent);
    }
}
