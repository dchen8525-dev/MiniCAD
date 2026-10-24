package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal vertex marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
public final class StepVertex extends AbstractStepEntity {
    public StepVertex(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
