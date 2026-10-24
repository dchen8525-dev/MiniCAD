package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "yieldStress", yieldStress);
    }
}
