package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEA_YIELD_STRESS.
 * Yield stress property for FEA.
 */
public final class StepFeaYieldStress extends AbstractStepEntity {
    private final double yieldStress;

    public StepFeaYieldStress(int id, String name, double yieldStress) {
        super(id, name);
        this.yieldStress = yieldStress;
    }

    public double getYieldStress() {
        return yieldStress;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("yieldStress", yieldStress);
        return state;
    }
}
