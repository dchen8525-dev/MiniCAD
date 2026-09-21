package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal derived unit element.
 *
 * @param id STEP instance id
 * @param unit referenced unit
 * @param exponent exponent value
 */
public final class StepDerivedUnitElement extends AbstractStepEntity {
    private final StepEntity unit;
    private final double exponent;

    public StepDerivedUnitElement(int id, StepEntity unit, double exponent) {
        super(id, "");
        this.unit = unit;
        this.exponent = exponent;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("unit", unit);
        state.put("exponent", exponent);
        return state;
    }
}
