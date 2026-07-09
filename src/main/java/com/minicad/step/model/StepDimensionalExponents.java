package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal DIMENSIONAL_EXPONENTS unit-dimension metadata.
 */
/**
 * Minimal DIMENSIONAL_EXPONENTS unit-dimension metadata.
 */
public final class StepDimensionalExponents implements StepEntity {
    private final int id;
    private final double lengthExponent;
    private final double massExponent;
    private final double timeExponent;
    private final double electricCurrentExponent;
    private final double thermodynamicTemperatureExponent;
    private final double amountOfSubstanceExponent;
    private final double luminousIntensityExponent;

    public StepDimensionalExponents(int id, double lengthExponent, double massExponent, double timeExponent, double electricCurrentExponent, double thermodynamicTemperatureExponent, double amountOfSubstanceExponent, double luminousIntensityExponent) {
        this.id = id;
        this.lengthExponent = lengthExponent;
        this.massExponent = massExponent;
        this.timeExponent = timeExponent;
        this.electricCurrentExponent = electricCurrentExponent;
        this.thermodynamicTemperatureExponent = thermodynamicTemperatureExponent;
        this.amountOfSubstanceExponent = amountOfSubstanceExponent;
        this.luminousIntensityExponent = luminousIntensityExponent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public double getLengthExponent() {
        return lengthExponent;
    }

    public double getMassExponent() {
        return massExponent;
    }

    public double getTimeExponent() {
        return timeExponent;
    }

    public double getElectricCurrentExponent() {
        return electricCurrentExponent;
    }

    public double getThermodynamicTemperatureExponent() {
        return thermodynamicTemperatureExponent;
    }

    public double getAmountOfSubstanceExponent() {
        return amountOfSubstanceExponent;
    }

    public double getLuminousIntensityExponent() {
        return luminousIntensityExponent;
    }

    // Record-style accessors
    public double lengthExponent() { return getLengthExponent(); }
    public double massExponent() { return getMassExponent(); }
    public double timeExponent() { return getTimeExponent(); }
    public double electricCurrentExponent() { return getElectricCurrentExponent(); }
    public double thermodynamicTemperatureExponent() { return getThermodynamicTemperatureExponent(); }
    public double amountOfSubstanceExponent() { return getAmountOfSubstanceExponent(); }
    public double luminousIntensityExponent() { return getLuminousIntensityExponent(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDimensionalExponents that = (StepDimensionalExponents) o;
        return id == that.id && lengthExponent == that.lengthExponent && massExponent == that.massExponent && timeExponent == that.timeExponent && electricCurrentExponent == that.electricCurrentExponent && thermodynamicTemperatureExponent == that.thermodynamicTemperatureExponent && amountOfSubstanceExponent == that.amountOfSubstanceExponent && luminousIntensityExponent == that.luminousIntensityExponent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lengthExponent, massExponent, timeExponent, electricCurrentExponent, thermodynamicTemperatureExponent, amountOfSubstanceExponent, luminousIntensityExponent);
    }

    @Override
    public String toString() {
        return "StepDimensionalExponents{" + "id=" + id + "lengthExponent=" + lengthExponent + "massExponent=" + massExponent + "timeExponent=" + timeExponent + "electricCurrentExponent=" + electricCurrentExponent + "thermodynamicTemperatureExponent=" + thermodynamicTemperatureExponent + "amountOfSubstanceExponent=" + amountOfSubstanceExponent + "luminousIntensityExponent=" + luminousIntensityExponent + "}";
    }
}
