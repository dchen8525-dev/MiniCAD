package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "ultimateStress", ultimateStress);
    }
}
