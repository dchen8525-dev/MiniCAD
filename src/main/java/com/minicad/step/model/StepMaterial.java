package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MATERIAL.
 * A material definition entity.
 */
public final class StepMaterial extends AbstractStepEntity {
    private final String materialType;
    private final double youngsModulus;
    private final double poissonsRatio;
    private final double density;

    public StepMaterial(int id, String name, String materialType, double youngsModulus, double poissonsRatio, double density) {
        super(id, name);
        this.materialType = materialType;
        this.youngsModulus = youngsModulus;
        this.poissonsRatio = poissonsRatio;
        this.density = density;
    }

    public String getMaterialType() {
        return materialType;
    }

    public double getYoungsModulus() {
        return youngsModulus;
    }

    public double getPoissonsRatio() {
        return poissonsRatio;
    }

    public double getDensity() {
        return density;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("materialType", materialType);
        state.put("youngsModulus", youngsModulus);
        state.put("poissonsRatio", poissonsRatio);
        state.put("density", density);
        return state;
    }
}
