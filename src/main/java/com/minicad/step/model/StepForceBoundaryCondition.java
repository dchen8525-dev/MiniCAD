package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FORCE_BOUNDARY_CONDITION.
 * Force boundary condition for FEA.
 */
public final class StepForceBoundaryCondition extends AbstractStepEntity {
    private final StepEntity appliedTo;
    private final double fx;
    private final double fy;
    private final double fz;

    public StepForceBoundaryCondition(int id, String name, StepEntity appliedTo, double fx, double fy, double fz) {
        super(id, name);
        this.appliedTo = appliedTo;
        this.fx = fx;
        this.fy = fy;
        this.fz = fz;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("appliedTo", appliedTo);
        state.put("fx", fx);
        state.put("fy", fy);
        state.put("fz", fz);
        return state;
    }
}
