package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FEA_MASS_DENSITY.
 * Mass density property for FEA.
 */
/**
 * Resolved FEA_MASS_DENSITY.
 * Mass density property for FEA.
 */
public final class StepFeaMassDensity implements StepEntity {
    private final int id;
    private final String name;
    private final double density;

    public StepFeaMassDensity(int id, String name, double density) {
        this.id = id;
        this.name = name;
        this.density = density;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDensity() {
        return density;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaMassDensity that = (StepFeaMassDensity) o;
        return id == that.id && Objects.equals(name, that.name) && density == that.density;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, density);
    }

    @Override
    public String toString() {
        return "StepFeaMassDensity{" + "id=" + id + "name=" + name + "density=" + density + "}";
    }
}
