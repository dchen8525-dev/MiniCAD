package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SPECIFIC_HIGHER_USAGE_OCCURRENCE.
 * A specific higher usage occurrence (SHUO) in assembly structure.
 */
public final class StepSpecificHigherUsageOccurrence extends AbstractStepEntity {
    private final StepEntity parent;
    private final StepEntity child;

    public StepSpecificHigherUsageOccurrence(int id, String name, StepEntity parent, StepEntity child) {
        super(id, name);
        this.parent = parent;
        this.child = child;
    }

    public StepEntity getParent() {
        return parent;
    }

    public StepEntity getChild() {
        return child;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("parent", parent);
        state.put("child", child);
        return state;
    }
}
