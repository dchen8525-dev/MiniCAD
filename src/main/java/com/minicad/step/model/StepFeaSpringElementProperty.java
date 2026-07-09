package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved FEA_SPRING_ELEMENT_PROPERTY.
 */
/**
 * Resolved FEA_SPRING_ELEMENT_PROPERTY.
 */
public final class StepFeaSpringElementProperty implements StepEntity {
    private final int id;
    private final String name;
    private final double springConstant;
    private final StepEntity material;

    public StepFeaSpringElementProperty(int id, String name, double springConstant, StepEntity material) {
        this.id = id;
        this.name = name;
        this.springConstant = springConstant;
        this.material = material;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSpringConstant() {
        return springConstant;
    }

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaSpringElementProperty that = (StepFeaSpringElementProperty) o;
        return id == that.id && Objects.equals(name, that.name) && springConstant == that.springConstant && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, springConstant, material);
    }

    @Override
    public String toString() {
        return "StepFeaSpringElementProperty{" + "id=" + id + "name=" + name + "springConstant=" + springConstant + "material=" + material + "}";
    }
}
