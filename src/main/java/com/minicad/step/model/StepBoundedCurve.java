package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal bounded-curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public final class StepBoundedCurve extends AbstractStepEntity {
    public StepBoundedCurve(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
