package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ACCELERATION_BOUNDARY_CONDITION.
 * Acceleration boundary condition for FEA.
 */
/**
 * Resolved ACCELERATION_BOUNDARY_CONDITION.
 * Acceleration boundary condition for FEA.
 */
public final class StepAccelerationBoundaryCondition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity appliedTo;
    private final double ax;
    private final double ay;
    private final double az;

    public StepAccelerationBoundaryCondition(int id, String name, StepEntity appliedTo, double ax, double ay, double az) {
        this.id = id;
        this.name = name;
        this.appliedTo = appliedTo;
        this.ax = ax;
        this.ay = ay;
        this.az = az;
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

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }

    public double getAz() {
        return az;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAccelerationBoundaryCondition that = (StepAccelerationBoundaryCondition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(appliedTo, that.appliedTo) && ax == that.ax && ay == that.ay && az == that.az;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, appliedTo, ax, ay, az);
    }

    @Override
    public String toString() {
        return "StepAccelerationBoundaryCondition{" + "id=" + id + "name=" + name + "appliedTo=" + appliedTo + "ax=" + ax + "ay=" + ay + "az=" + az + "}";
    }
}
