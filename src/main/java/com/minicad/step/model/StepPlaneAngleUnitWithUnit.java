package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved PLANE_ANGLE_UNIT_WITH_UNIT.
 */
public final class StepPlaneAngleUnitWithUnit extends AbstractStepEntity {
    private final StepEntity angleUnit;
    private final StepEntity unitComponent;

    public StepPlaneAngleUnitWithUnit(int id, String name, StepEntity angleUnit, StepEntity unitComponent) {
        super(id, name);
        this.angleUnit = angleUnit;
        this.unitComponent = unitComponent;
    }

    public StepEntity getAngleUnit() {
        return angleUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "angleUnit", angleUnit, "unitComponent", unitComponent);
    }
}
