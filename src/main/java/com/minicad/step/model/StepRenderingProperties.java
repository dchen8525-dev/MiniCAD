package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepRenderingProperties implements StepEntity {
    private final int id;
    private final String name;
    private final double specularExponent;
    private final double specularRoughness;

    public StepRenderingProperties(int id, String name, double specularExponent, double specularRoughness) {
        this.id = id;
        this.name = name;
        this.specularExponent = specularExponent;
        this.specularRoughness = specularRoughness;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSpecularExponent() {
        return specularExponent;
    }

    public double getSpecularRoughness() {
        return specularRoughness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRenderingProperties that = (StepRenderingProperties) o;
        return id == that.id && Objects.equals(name, that.name) && specularExponent == that.specularExponent && specularRoughness == that.specularRoughness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specularExponent, specularRoughness);
    }

    @Override
    public String toString() {
        return "StepRenderingProperties{" + "id=" + id + "name=" + name + "specularExponent=" + specularExponent + "specularRoughness=" + specularRoughness + "}";
    }
}