package com.minicad.step.model.technical.unit;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved MASS_UNIT_WITH_UNIT.
 */
/**
 * Resolved MASS_UNIT_WITH_UNIT.
 */
public final class StepMassUnitWithUnit implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity massUnit;
    private final StepEntity unitComponent;

    public StepMassUnitWithUnit(int id, String name, StepEntity massUnit, StepEntity unitComponent) {
        this.id = id;
        this.name = name;
        this.massUnit = massUnit;
        this.unitComponent = unitComponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getMassUnit() {
        return massUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMassUnitWithUnit that = (StepMassUnitWithUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(massUnit, that.massUnit) && Objects.equals(unitComponent, that.unitComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, massUnit, unitComponent);
    }

    @Override
    public String toString() {
        return "StepMassUnitWithUnit{" + "id=" + id + "name=" + name + "massUnit=" + massUnit + "unitComponent=" + unitComponent + "}";
    }
}
