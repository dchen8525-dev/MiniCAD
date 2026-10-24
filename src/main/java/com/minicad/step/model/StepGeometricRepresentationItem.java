package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal geometric representation item marker.
 *
 * @param id STEP instance id
 * @param name inherited representation-item name when available
 */
public final class StepGeometricRepresentationItem extends AbstractStepEntity {
    public StepGeometricRepresentationItem(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
