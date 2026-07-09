package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved AREA_UNIT_WITH_UNIT.
 */
/**
 * Resolved AREA_UNIT_WITH_UNIT.
 */
public final class StepAreaUnitWithUnit implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity areaUnit;
    private final StepEntity unitComponent;

    public StepAreaUnitWithUnit(int id, String name, StepEntity areaUnit, StepEntity unitComponent) {
        this.id = id;
        this.name = name;
        this.areaUnit = areaUnit;
        this.unitComponent = unitComponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getAreaUnit() {
        return areaUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAreaUnitWithUnit that = (StepAreaUnitWithUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(areaUnit, that.areaUnit) && Objects.equals(unitComponent, that.unitComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, areaUnit, unitComponent);
    }

    @Override
    public String toString() {
        return "StepAreaUnitWithUnit{" + "id=" + id + "name=" + name + "areaUnit=" + areaUnit + "unitComponent=" + unitComponent + "}";
    }
}
