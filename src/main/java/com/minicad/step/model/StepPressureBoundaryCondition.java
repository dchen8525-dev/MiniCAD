package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved PRESSURE_BOUNDARY_CONDITION.
 * Pressure boundary condition for FEA.
 */
/**
 * Resolved PRESSURE_BOUNDARY_CONDITION.
 * Pressure boundary condition for FEA.
 */
public final class StepPressureBoundaryCondition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity appliedTo;
    private final double pressure;

    public StepPressureBoundaryCondition(int id, String name, StepEntity appliedTo, double pressure) {
        this.id = id;
        this.name = name;
        this.appliedTo = appliedTo;
        this.pressure = pressure;
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

    public double getPressure() {
        return pressure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPressureBoundaryCondition that = (StepPressureBoundaryCondition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(appliedTo, that.appliedTo) && pressure == that.pressure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, appliedTo, pressure);
    }

    @Override
    public String toString() {
        return "StepPressureBoundaryCondition{" + "id=" + id + "name=" + name + "appliedTo=" + appliedTo + "pressure=" + pressure + "}";
    }
}
