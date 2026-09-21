package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("appliedTo", appliedTo);
        state.put("pressure", pressure);
        return state;
    }
}
