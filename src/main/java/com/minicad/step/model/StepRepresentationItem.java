package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal representation item marker.
 *
 * @param id STEP instance id
 * @param name item name
 */
public final class StepRepresentationItem extends AbstractStepEntity {
    public StepRepresentationItem(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
