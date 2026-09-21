package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FILL_AREA_SHAPE_USE.
 * A fill area shape use entity.
 */
public final class StepFillAreaShapeUse extends AbstractStepEntity {
    private final StepEntity fillArea;

    public StepFillAreaShapeUse(int id, String name, StepEntity fillArea) {
        super(id, name);
        this.fillArea = fillArea;
    }

    public StepEntity getFillArea() {
        return fillArea;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("fillArea", fillArea);
        return state;
    }
}
