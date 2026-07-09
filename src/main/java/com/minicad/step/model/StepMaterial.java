package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MATERIAL.
 * A material definition entity.
 */
/**
 * Resolved MATERIAL.
 * A material definition entity.
 */
public final class StepMaterial implements StepEntity {
    private final int id;
    private final String name;
    private final String materialType;
    private final double youngsModulus;
    private final double poissonsRatio;
    private final double density;

    public StepMaterial(int id, String name, String materialType, double youngsModulus, double poissonsRatio, double density) {
        this.id = id;
        this.name = name;
        this.materialType = materialType;
        this.youngsModulus = youngsModulus;
        this.poissonsRatio = poissonsRatio;
        this.density = density;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMaterial that = (StepMaterial) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(materialType, that.materialType) && youngsModulus == that.youngsModulus && poissonsRatio == that.poissonsRatio && density == that.density;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, materialType, youngsModulus, poissonsRatio, density);
    }

    @Override
    public String toString() {
        return "StepMaterial{" + "id=" + id + "name=" + name + "materialType=" + materialType + "youngsModulus=" + youngsModulus + "poissonsRatio=" + poissonsRatio + "density=" + density + "}";
    }
}
