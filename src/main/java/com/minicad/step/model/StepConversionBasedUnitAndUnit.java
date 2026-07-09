package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * Resolved CONVERSION_BASED_UNIT_AND_UNIT.
 */
/**
 * Resolved CONVERSION_BASED_UNIT_AND_UNIT.
 */
public final class StepConversionBasedUnitAndUnit implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity convertedUnit;
    private final StepEntity unitComponent;

    public StepConversionBasedUnitAndUnit(int id, String name, StepEntity convertedUnit, StepEntity unitComponent) {
        this.id = id;
        this.name = name;
        this.convertedUnit = convertedUnit;
        this.unitComponent = unitComponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getConvertedUnit() {
        return convertedUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConversionBasedUnitAndUnit that = (StepConversionBasedUnitAndUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(convertedUnit, that.convertedUnit) && Objects.equals(unitComponent, that.unitComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, convertedUnit, unitComponent);
    }

    @Override
    public String toString() {
        return "StepConversionBasedUnitAndUnit{" + "id=" + id + "name=" + name + "convertedUnit=" + convertedUnit + "unitComponent=" + unitComponent + "}";
    }
}
