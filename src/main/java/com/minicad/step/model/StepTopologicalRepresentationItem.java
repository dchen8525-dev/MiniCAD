package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal topological representation item marker.
 *
 * @param id STEP instance id
 * @param name item name
 */
public final class StepTopologicalRepresentationItem extends AbstractStepEntity {
    public StepTopologicalRepresentationItem(int id, String name) {
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
