package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved BOUNDING_BOX.
 * An axis-aligned bounding box.
 */
public final class StepBoundingBox extends AbstractStepEntity {
    private final StepEntity corner1;
    private final StepEntity corner2;

    public StepBoundingBox(int id, String name, StepEntity corner1, StepEntity corner2) {
        super(id, name);
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public StepEntity getCorner1() {
        return corner1;
    }

    public StepEntity getCorner2() {
        return corner2;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("corner1", corner1);
        state.put("corner2", corner2);
        return state;
    }
}
