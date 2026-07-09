package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FEA_ULTIMATE_STRESS.
 * Ultimate stress property for FEA.
 */
/**
 * Resolved FEA_ULTIMATE_STRESS.
 * Ultimate stress property for FEA.
 */
public final class StepFeaUltimateStress implements StepEntity {
    private final int id;
    private final String name;
    private final double ultimateStress;

    public StepFeaUltimateStress(int id, String name, double ultimateStress) {
        this.id = id;
        this.name = name;
        this.ultimateStress = ultimateStress;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getUltimateStress() {
        return ultimateStress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaUltimateStress that = (StepFeaUltimateStress) o;
        return id == that.id && Objects.equals(name, that.name) && ultimateStress == that.ultimateStress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ultimateStress);
    }

    @Override
    public String toString() {
        return "StepFeaUltimateStress{" + "id=" + id + "name=" + name + "ultimateStress=" + ultimateStress + "}";
    }
}
