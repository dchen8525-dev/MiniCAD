package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal conversion-based unit with offset definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as THERMODYNAMIC_TEMPERATURE_UNIT
 * @param conversionFactor referenced conversion factor
 * @param conversionOffset scalar offset
 */
/**
 * Minimal conversion-based unit with offset definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as THERMODYNAMIC_TEMPERATURE_UNIT
 * @param conversionFactor referenced conversion factor
 * @param conversionOffset scalar offset
 */
public final class StepConversionBasedUnitWithOffset implements StepEntity {
    private final int id;
    private final String name;
    private final String unitKind;
    private final StepMeasureWithUnit conversionFactor;
    private final double conversionOffset;

    public StepConversionBasedUnitWithOffset(int id, String name, String unitKind, StepMeasureWithUnit conversionFactor, double conversionOffset) {
        this.id = id;
        this.name = name;
        this.unitKind = unitKind;
        this.conversionFactor = conversionFactor;
        this.conversionOffset = conversionOffset;
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

    public double getConversionOffset() {
        return conversionOffset;
    }

    // Record-style accessor
    public StepMeasureWithUnit conversionFactor() {
        return conversionFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConversionBasedUnitWithOffset that = (StepConversionBasedUnitWithOffset) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(unitKind, that.unitKind) && Objects.equals(conversionFactor, that.conversionFactor) && conversionOffset == that.conversionOffset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitKind, conversionFactor, conversionOffset);
    }

    @Override
    public String toString() {
        return "StepConversionBasedUnitWithOffset{" + "id=" + id + "name=" + name + "unitKind=" + unitKind + "conversionFactor=" + conversionFactor + "conversionOffset=" + conversionOffset + "}";
    }
}
