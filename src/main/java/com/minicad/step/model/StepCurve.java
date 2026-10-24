package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public final class StepCurve extends AbstractStepEntity {
    public StepCurve(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
