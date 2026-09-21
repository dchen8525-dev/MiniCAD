package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEA_ULTIMATE_STRESS.
 * Ultimate stress property for FEA.
 */
public final class StepFeaUltimateStress extends AbstractStepEntity {
    private final double ultimateStress;

    public StepFeaUltimateStress(int id, String name, double ultimateStress) {
        super(id, name);
        this.ultimateStress = ultimateStress;
    }

    public double getUltimateStress() {
        return ultimateStress;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("ultimateStress", ultimateStress);
        return state;
    }
}
