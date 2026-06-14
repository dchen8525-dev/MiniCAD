package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved VELOCITY_BOUNDARY_CONDITION.
 * Velocity boundary condition for FEA.
 */
/**
 * Resolved VELOCITY_BOUNDARY_CONDITION.
 * Velocity boundary condition for FEA.
 */
public final class StepVelocityBoundaryCondition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity appliedTo;
    private final double vx;
    private final double vy;
    private final double vz;

    public StepVelocityBoundaryCondition(int id, String name, StepEntity appliedTo, double vx, double vy, double vz) {
        this.id = id;
        this.name = name;
        this.appliedTo = appliedTo;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
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

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getVz() {
        return vz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVelocityBoundaryCondition that = (StepVelocityBoundaryCondition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(appliedTo, that.appliedTo) && vx == that.vx && vy == that.vy && vz == that.vz;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, appliedTo, vx, vy, vz);
    }

    @Override
    public String toString() {
        return "StepVelocityBoundaryCondition{" + "id=" + id + "name=" + name + "appliedTo=" + appliedTo + "vx=" + vx + "vy=" + vy + "vz=" + vz + "}";
    }
}
