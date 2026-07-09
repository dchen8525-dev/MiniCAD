package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved DISPLACEMENT_BOUNDARY_CONDITION.
 * Displacement boundary condition for FEA.
 */
/**
 * Resolved DISPLACEMENT_BOUNDARY_CONDITION.
 * Displacement boundary condition for FEA.
 */
public final class StepDisplacementBoundaryCondition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity appliedTo;
    private final double dx;
    private final double dy;
    private final double dz;

    public StepDisplacementBoundaryCondition(int id, String name, StepEntity appliedTo, double dx, double dy, double dz) {
        this.id = id;
        this.name = name;
        this.appliedTo = appliedTo;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
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

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public double getDz() {
        return dz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDisplacementBoundaryCondition that = (StepDisplacementBoundaryCondition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(appliedTo, that.appliedTo) && dx == that.dx && dy == that.dy && dz == that.dz;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, appliedTo, dx, dy, dz);
    }

    @Override
    public String toString() {
        return "StepDisplacementBoundaryCondition{" + "id=" + id + "name=" + name + "appliedTo=" + appliedTo + "dx=" + dx + "dy=" + dy + "dz=" + dz + "}";
    }
}
