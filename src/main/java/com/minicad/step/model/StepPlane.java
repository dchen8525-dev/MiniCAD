package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PLANE.
 *
 * @param id step id
 * @param name step label
 * @param position plane placement
 */
public final class StepPlane extends AbstractStepEntity {
    private final StepAxis2Placement3D position;

    public StepPlane(int id, String name, StepAxis2Placement3D position) {
        super(id, name);
        this.position = position;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement3D position() { return getPosition(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        return state;
    }
}
