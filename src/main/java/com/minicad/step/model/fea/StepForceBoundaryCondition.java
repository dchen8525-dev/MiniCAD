package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FORCE_BOUNDARY_CONDITION.
 * Force boundary condition for FEA.
 */
/**
 * Resolved FORCE_BOUNDARY_CONDITION.
 * Force boundary condition for FEA.
 */
public final class StepForceBoundaryCondition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity appliedTo;
    private final double fx;
    private final double fy;
    private final double fz;

    public StepForceBoundaryCondition(int id, String name, StepEntity appliedTo, double fx, double fy, double fz) {
        this.id = id;
        this.name = name;
        this.appliedTo = appliedTo;
        this.fx = fx;
        this.fy = fy;
        this.fz = fz;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getAppliedTo() {
        return appliedTo;
    }

    public double getFx() {
        return fx;
    }

    public double getFy() {
        return fy;
    }

    public double getFz() {
        return fz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepForceBoundaryCondition that = (StepForceBoundaryCondition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(appliedTo, that.appliedTo) && fx == that.fx && fy == that.fy && fz == that.fz;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, appliedTo, fx, fy, fz);
    }

    @Override
    public String toString() {
        return "StepForceBoundaryCondition{" + "id=" + id + "name=" + name + "appliedTo=" + appliedTo + "fx=" + fx + "fy=" + fy + "fz=" + fz + "}";
    }
}
