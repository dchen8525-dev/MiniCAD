package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FEA_NON_LINEAR_MATERIAL.
 * A non-linear material definition for FEA.
 */
/**
 * Resolved FEA_NON_LINEAR_MATERIAL.
 * A non-linear material definition for FEA.
 */
public final class StepFeaNonLinearMaterial implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity material;
    private final String nonLinearModel;

    public StepFeaNonLinearMaterial(int id, String name, StepEntity material, String nonLinearModel) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.nonLinearModel = nonLinearModel;
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

    public String getNonLinearModel() {
        return nonLinearModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaNonLinearMaterial that = (StepFeaNonLinearMaterial) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(material, that.material) && Objects.equals(nonLinearModel, that.nonLinearModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, material, nonLinearModel);
    }

    @Override
    public String toString() {
        return "StepFeaNonLinearMaterial{" + "id=" + id + "name=" + name + "material=" + material + "nonLinearModel=" + nonLinearModel + "}";
    }
}
