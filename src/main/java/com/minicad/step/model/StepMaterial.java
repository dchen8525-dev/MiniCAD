package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "materialType", materialType,
                "youngsModulus", youngsModulus, "poissonsRatio", poissonsRatio, "density", density);
    }
}
