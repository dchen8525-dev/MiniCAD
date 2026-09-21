package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal annotation fill area geometry.
 *
 * @param id STEP instance id
 * @param name representation item name
 * @param boundaries fill boundaries
 */
public final class StepAnnotationFillArea extends AbstractStepEntity {
    private final List<StepEntity> boundaries;

    public StepAnnotationFillArea(int id, String name, List<StepEntity> boundaries) {
        super(id, name);
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
    }

    public List<StepEntity> getBoundaries() {
        return boundaries;
    }

    // Record-style accessor
    public List<StepEntity> boundaries() {
        return boundaries;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("boundaries", boundaries);
        return state;
    }
}
