package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved CSG_PRIMITIVE_3D.
 */
public final class StepCsgPrimitive3D extends AbstractStepEntity {
    private final StepEntity position;

    public StepCsgPrimitive3D(int id, String name, StepEntity position) {
        super(id, name);
        this.position = position;
    }

    public StepEntity getPosition() {
        return position;
    }

    // Record-style accessor
    public StepEntity position() { return getPosition(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "position", position);
    }
}
