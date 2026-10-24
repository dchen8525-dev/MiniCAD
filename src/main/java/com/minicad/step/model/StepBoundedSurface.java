package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal bounded-surface marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public final class StepBoundedSurface extends AbstractStepEntity {
    public StepBoundedSurface(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
