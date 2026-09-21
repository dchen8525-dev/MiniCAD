package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DISPLACEMENT_BOUNDARY_CONDITION.
 * Displacement boundary condition for FEA.
 */
public final class StepDisplacementBoundaryCondition extends AbstractStepEntity {
    private final StepEntity appliedTo;
    private final double dx;
    private final double dy;
    private final double dz;

    public StepDisplacementBoundaryCondition(int id, String name, StepEntity appliedTo, double dx, double dy, double dz) {
        super(id, name);
        this.appliedTo = appliedTo;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("appliedTo", appliedTo);
        state.put("dx", dx);
        state.put("dy", dy);
        state.put("dz", dz);
        return state;
    }
}
