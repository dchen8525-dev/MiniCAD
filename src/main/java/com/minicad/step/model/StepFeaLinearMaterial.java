package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FEA_LINEAR_MATERIAL.
 * A linear material definition for FEA.
 */
/**
 * Resolved FEA_LINEAR_MATERIAL.
 * A linear material definition for FEA.
 */
public final class StepFeaLinearMaterial implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity material;
    private final double youngsModulus;
    private final double poissonsRatio;

    public StepFeaLinearMaterial(int id, String name, StepEntity material, double youngsModulus, double poissonsRatio) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.youngsModulus = youngsModulus;
        this.poissonsRatio = poissonsRatio;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaLinearMaterial that = (StepFeaLinearMaterial) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(material, that.material) && youngsModulus == that.youngsModulus && poissonsRatio == that.poissonsRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, material, youngsModulus, poissonsRatio);
    }

    @Override
    public String toString() {
        return "StepFeaLinearMaterial{" + "id=" + id + "name=" + name + "material=" + material + "youngsModulus=" + youngsModulus + "poissonsRatio=" + poissonsRatio + "}";
    }
}
