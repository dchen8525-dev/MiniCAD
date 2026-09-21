package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved AXIS2_PLACEMENT_2D.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param refDirection local x direction
 */
public final class StepAxis2Placement2D extends AbstractStepEntity {
    private final StepCartesianPoint location;
    private final StepDirection refDirection;

    public StepAxis2Placement2D(int id, String name, StepCartesianPoint location, StepDirection refDirection) {
        super(id, name);
        this.location = location;
        this.refDirection = refDirection;
    }

    public StepCartesianPoint getLocation() {
        return location;
    }

    public StepDirection getRefDirection() {
        return refDirection;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint location() { return getLocation(); }
    public StepDirection refDirection() { return getRefDirection(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("location", location);
        state.put("refDirection", refDirection);
        return state;
    }
}
