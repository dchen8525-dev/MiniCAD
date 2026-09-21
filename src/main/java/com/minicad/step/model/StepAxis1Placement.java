package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved AXIS1_PLACEMENT.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param axis axis direction
 */
public final class StepAxis1Placement extends AbstractStepEntity {
    private final StepCartesianPoint location;
    private final StepDirection axis;

    public StepAxis1Placement(int id, String name, StepCartesianPoint location, StepDirection axis) {
        super(id, name);
        this.location = location;
        this.axis = axis;
    }

    public StepCartesianPoint getLocation() {
        return location;
    }

    public StepDirection getAxis() {
        return axis;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint location() { return getLocation(); }
    public StepDirection axis() { return getAxis(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("location", location);
        state.put("axis", axis);
        return state;
    }
}
