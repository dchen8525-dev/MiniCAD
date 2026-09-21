package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEA_AXIS_2_PLACEMENT_3D.
 * A 3D axis placement for finite element coordinate systems.
 */
public final class StepFeaAxis2Placement3d extends AbstractStepEntity {
    private final StepEntity location;
    private final StepEntity axis;
    private final StepEntity refDirection;

    public StepFeaAxis2Placement3d(int id, String name, StepEntity location, StepEntity axis, StepEntity refDirection) {
        super(id, name);
        this.location = location;
        this.axis = axis;
        this.refDirection = refDirection;
    }

    public StepEntity getLocation() {
        return location;
    }

    public StepEntity getAxis() {
        return axis;
    }

    public StepEntity getRefDirection() {
        return refDirection;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity location() { return getLocation(); }
    public StepEntity axis() { return getAxis(); }
    public StepEntity refDirection() { return getRefDirection(); }

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
