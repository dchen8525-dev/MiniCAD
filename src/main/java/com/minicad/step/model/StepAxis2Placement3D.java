package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved AXIS2_PLACEMENT_3D with explicit axis and ref direction.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param axis local Z direction
 * @param refDirection local X direction
 */
public final class StepAxis2Placement3D extends AbstractStepEntity {
    private final StepCartesianPoint location;
    private final StepDirection axis;
    private final StepDirection refDirection;

    public StepAxis2Placement3D(int id, String name, StepCartesianPoint location, StepDirection axis, StepDirection refDirection) {
        super(id, name);
        this.location = location;
        this.axis = axis;
        this.refDirection = refDirection;
    }

    public StepCartesianPoint getLocation() {
        return location;
    }

    public StepDirection getAxis() {
        return axis;
    }

    public StepDirection getRefDirection() {
        return refDirection;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint location() { return getLocation(); }
    public StepDirection axis() { return getAxis(); }
    public StepDirection refDirection() { return getRefDirection(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("location", location);
        state.put("axis", axis);
        state.put("refDirection", refDirection);
        return state;
    }
}
