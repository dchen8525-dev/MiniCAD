package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved PRESSURE_BOUNDARY_CONDITION.
 * Pressure boundary condition for FEA.
 */
public final class StepPressureBoundaryCondition extends AbstractStepEntity {
    private final StepEntity appliedTo;
    private final double pressure;

    public StepPressureBoundaryCondition(int id, String name, StepEntity appliedTo, double pressure) {
        super(id, name);
        this.appliedTo = appliedTo;
        this.pressure = pressure;
    }

    public StepEntity getAppliedTo() {
        return appliedTo;
    }

    public double getPressure() {
        return pressure;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "appliedTo", appliedTo, "pressure", pressure);
    }
}
