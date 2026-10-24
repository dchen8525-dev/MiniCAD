package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved AREA_UNIT_WITH_UNIT.
 */
public final class StepAreaUnitWithUnit extends AbstractStepEntity {
    private final StepEntity areaUnit;
    private final StepEntity unitComponent;

    public StepAreaUnitWithUnit(int id, String name, StepEntity areaUnit, StepEntity unitComponent) {
        super(id, name);
        this.areaUnit = areaUnit;
        this.unitComponent = unitComponent;
    }

    public StepEntity getAreaUnit() {
        return areaUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "areaUnit", areaUnit, "unitComponent", unitComponent);
    }
}
