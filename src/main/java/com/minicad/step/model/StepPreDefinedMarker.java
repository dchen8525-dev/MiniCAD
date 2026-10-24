package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal PRE_DEFINED_MARKER.
 *
 * @param id step id
 * @param name predefined marker name
 */
public final class StepPreDefinedMarker extends AbstractStepEntity {
    public StepPreDefinedMarker(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
