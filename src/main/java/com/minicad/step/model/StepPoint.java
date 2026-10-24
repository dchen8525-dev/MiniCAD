package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal point marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public final class StepPoint extends AbstractStepEntity {
    public StepPoint(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
