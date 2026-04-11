package com.minicad.step.model;

/**
 * Minimal DIMENSIONAL_EXPONENTS unit-dimension metadata.
 */
public record StepDimensionalExponents(
        int id,
        double lengthExponent,
        double massExponent,
        double timeExponent,
        double electricCurrentExponent,
        double thermodynamicTemperatureExponent,
        double amountOfSubstanceExponent,
        double luminousIntensityExponent
) implements StepEntity {

    @Override
    public String name() {
        return "DIMENSIONAL_EXPONENTS";
    }
}
