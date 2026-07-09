package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved FEA_YIELD_STRESS.
 * Yield stress property for FEA.
 */
/**
 * Resolved FEA_YIELD_STRESS.
 * Yield stress property for FEA.
 */
public final class StepFeaYieldStress implements StepEntity {
    private final int id;
    private final String name;
    private final double yieldStress;

    public StepFeaYieldStress(int id, String name, double yieldStress) {
        this.id = id;
        this.name = name;
        this.yieldStress = yieldStress;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getYieldStress() {
        return yieldStress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaYieldStress that = (StepFeaYieldStress) o;
        return id == that.id && Objects.equals(name, that.name) && yieldStress == that.yieldStress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, yieldStress);
    }

    @Override
    public String toString() {
        return "StepFeaYieldStress{" + "id=" + id + "name=" + name + "yieldStress=" + yieldStress + "}";
    }
}
