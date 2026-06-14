package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal conversion-based unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param conversionFactor referenced conversion factor
 */
/**
 * Minimal conversion-based unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param conversionFactor referenced conversion factor
 */
public final class StepConversionBasedUnit implements StepEntity {
    private final int id;
    private final String name;
    private final String unitKind;
    private final StepMeasureWithUnit conversionFactor;

    public StepConversionBasedUnit(int id, String name, String unitKind, StepMeasureWithUnit conversionFactor) {
        this.id = id;
        this.name = name;
        this.unitKind = unitKind;
        this.conversionFactor = conversionFactor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitKind() {
        return unitKind;
    }

    public StepMeasureWithUnit getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConversionBasedUnit that = (StepConversionBasedUnit) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(unitKind, that.unitKind) && Objects.equals(conversionFactor, that.conversionFactor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitKind, conversionFactor);
    }

    @Override
    public String toString() {
        return "StepConversionBasedUnit{" + "id=" + id + "name=" + name + "unitKind=" + unitKind + "conversionFactor=" + conversionFactor + "}";
    }
}
