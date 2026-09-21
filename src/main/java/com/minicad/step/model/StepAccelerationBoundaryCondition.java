package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ACCELERATION_BOUNDARY_CONDITION.
 * Acceleration boundary condition for FEA.
 */
public final class StepAccelerationBoundaryCondition extends AbstractStepEntity {
    private final StepEntity appliedTo;
    private final double ax;
    private final double ay;
    private final double az;

    public StepAccelerationBoundaryCondition(int id, String name, StepEntity appliedTo, double ax, double ay, double az) {
        super(id, name);
        this.appliedTo = appliedTo;
        this.ax = ax;
        this.ay = ay;
        this.az = az;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("appliedTo", appliedTo);
        state.put("ax", ax);
        state.put("ay", ay);
        state.put("az", az);
        return state;
    }
}
