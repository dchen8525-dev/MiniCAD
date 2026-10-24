package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved VELOCITY_BOUNDARY_CONDITION.
 * Velocity boundary condition for FEA.
 */
public final class StepVelocityBoundaryCondition extends AbstractStepEntity {
    private final StepEntity appliedTo;
    private final double vx;
    private final double vy;
    private final double vz;

    public StepVelocityBoundaryCondition(int id, String name, StepEntity appliedTo, double vx, double vy, double vz) {
        super(id, name);
        this.appliedTo = appliedTo;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "appliedTo", appliedTo, "vx", vx, "vy", vy, "vz", vz);
    }
}
