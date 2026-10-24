package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal face marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
public final class StepFace extends AbstractStepEntity {
    public StepFace(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
