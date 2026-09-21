package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PRE_DEFINED_SURFACE_SIDE_STYLE.
 *
 * @param id step id
 * @param name predefined surface side style name
 */
public final class StepPreDefinedSurfaceSideStyle extends AbstractStepEntity {
    public StepPreDefinedSurfaceSideStyle(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        return state;
    }
}
