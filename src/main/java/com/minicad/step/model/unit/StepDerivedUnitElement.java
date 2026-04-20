package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal derived unit element.
 *
 * @param id STEP instance id
 * @param unit referenced unit
 * @param exponent exponent value
 */
public record StepDerivedUnitElement(int id, StepEntity unit, double exponent) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
