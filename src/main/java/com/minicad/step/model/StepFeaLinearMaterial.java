package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEA_LINEAR_MATERIAL.
 * A linear material definition for FEA.
 */
public final class StepFeaLinearMaterial extends AbstractStepEntity {
    private final StepEntity material;
    private final double youngsModulus;
    private final double poissonsRatio;

    public StepFeaLinearMaterial(int id, String name, StepEntity material, double youngsModulus, double poissonsRatio) {
        super(id, name);
        this.material = material;
        this.youngsModulus = youngsModulus;
        this.poissonsRatio = poissonsRatio;
    }

    public StepEntity getMaterial() {
        return material;
    }

    public double getYoungsModulus() {
        return youngsModulus;
    }

    public double getPoissonsRatio() {
        return poissonsRatio;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("material", material);
        state.put("youngsModulus", youngsModulus);
        state.put("poissonsRatio", poissonsRatio);
        return state;
    }
}
