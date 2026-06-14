package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved LENGTH_UNIT_WITH_UNIT.
 */
/**
 * Resolved LENGTH_UNIT_WITH_UNIT.
 */
public final class StepLengthUnitWithUnit implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity lengthUnit;
    private final StepEntity unitComponent;

    public StepLengthUnitWithUnit(int id, String name, StepEntity lengthUnit, StepEntity unitComponent) {
        this.id = id;
        this.name = name;
        this.lengthUnit = lengthUnit;
        this.unitComponent = unitComponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getLengthUnit() {
        return lengthUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLengthUnitWithUnit that = (StepLengthUnitWithUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(lengthUnit, that.lengthUnit) && Objects.equals(unitComponent, that.unitComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lengthUnit, unitComponent);
    }

    @Override
    public String toString() {
        return "StepLengthUnitWithUnit{" + "id=" + id + "name=" + name + "lengthUnit=" + lengthUnit + "unitComponent=" + unitComponent + "}";
    }
}
