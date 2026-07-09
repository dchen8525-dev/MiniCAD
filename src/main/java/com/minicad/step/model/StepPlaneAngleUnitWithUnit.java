package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved PLANE_ANGLE_UNIT_WITH_UNIT.
 */
/**
 * Resolved PLANE_ANGLE_UNIT_WITH_UNIT.
 */
public final class StepPlaneAngleUnitWithUnit implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity angleUnit;
    private final StepEntity unitComponent;

    public StepPlaneAngleUnitWithUnit(int id, String name, StepEntity angleUnit, StepEntity unitComponent) {
        this.id = id;
        this.name = name;
        this.angleUnit = angleUnit;
        this.unitComponent = unitComponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getAngleUnit() {
        return angleUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlaneAngleUnitWithUnit that = (StepPlaneAngleUnitWithUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(angleUnit, that.angleUnit) && Objects.equals(unitComponent, that.unitComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, angleUnit, unitComponent);
    }

    @Override
    public String toString() {
        return "StepPlaneAngleUnitWithUnit{" + "id=" + id + "name=" + name + "angleUnit=" + angleUnit + "unitComponent=" + unitComponent + "}";
    }
}
