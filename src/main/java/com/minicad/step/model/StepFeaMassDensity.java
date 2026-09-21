package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEA_MASS_DENSITY.
 * Mass density property for FEA.
 */
public final class StepFeaMassDensity extends AbstractStepEntity {
    private final double density;

    public StepFeaMassDensity(int id, String name, double density) {
        super(id, name);
        this.density = density;
    }

    public double getDensity() {
        return density;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("density", density);
        return state;
    }
}
