package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DIMENSIONAL_EXPONENTS unit-dimension metadata.
 */
public final class StepDimensionalExponents extends AbstractStepEntity {
    private final double lengthExponent;
    private final double massExponent;
    private final double timeExponent;
    private final double electricCurrentExponent;
    private final double thermodynamicTemperatureExponent;
    private final double amountOfSubstanceExponent;
    private final double luminousIntensityExponent;

    public StepDimensionalExponents(int id, double lengthExponent, double massExponent, double timeExponent, double electricCurrentExponent, double thermodynamicTemperatureExponent, double amountOfSubstanceExponent, double luminousIntensityExponent) {
        super(id, "");
        this.lengthExponent = lengthExponent;
        this.massExponent = massExponent;
        this.timeExponent = timeExponent;
        this.electricCurrentExponent = electricCurrentExponent;
        this.thermodynamicTemperatureExponent = thermodynamicTemperatureExponent;
        this.amountOfSubstanceExponent = amountOfSubstanceExponent;
        this.luminousIntensityExponent = luminousIntensityExponent;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("lengthExponent", lengthExponent);
        state.put("massExponent", massExponent);
        state.put("timeExponent", timeExponent);
        state.put("electricCurrentExponent", electricCurrentExponent);
        state.put("thermodynamicTemperatureExponent", thermodynamicTemperatureExponent);
        state.put("amountOfSubstanceExponent", amountOfSubstanceExponent);
        state.put("luminousIntensityExponent", luminousIntensityExponent);
        return state;
    }
}
