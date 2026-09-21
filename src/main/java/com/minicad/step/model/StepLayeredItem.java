package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LAYERED_ITEM.
 * An item assigned to presentation layers.
 *
 * @param id STEP instance id
 * @param name item name
 * @param assignment layers assignment reference
 */
public final class StepLayeredItem extends AbstractStepEntity {
    private final StepEntity assignment;

    public StepLayeredItem(int id, String name, StepEntity assignment) {
        super(id, name);
        this.assignment = assignment;
    }

    public StepEntity getAssignment() {
        return assignment;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("assignment", assignment);
        return state;
    }
}
