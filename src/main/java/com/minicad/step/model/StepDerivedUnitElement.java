package com.minicad.step.model.technical.unit;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal derived unit element.
 *
 * @param id STEP instance id
 * @param unit referenced unit
 * @param exponent exponent value
 */
/**
 * Minimal derived unit element.
 *
 * @param id STEP instance id
 * @param unit referenced unit
 * @param exponent exponent value
 */
public final class StepDerivedUnitElement implements StepEntity {
    private final int id;
    private final StepEntity unit;
    private final double exponent;

    public StepDerivedUnitElement(int id, StepEntity unit, double exponent) {
        this.id = id;
        this.unit = unit;
        this.exponent = exponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepEntity getUnit() {
        return unit;
    }

    public double getExponent() {
        return exponent;
    }

    // Record-style accessor
    public StepEntity unit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDerivedUnitElement that = (StepDerivedUnitElement) o;
        return id == that.id && Objects.equals(unit, that.unit) && exponent == that.exponent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unit, exponent);
    }

    @Override
    public String toString() {
        return "StepDerivedUnitElement{" + "id=" + id + "unit=" + unit + "exponent=" + exponent + "}";
    }
}
