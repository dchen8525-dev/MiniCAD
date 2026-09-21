package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved USAGE_OCCURRENCE.
 * A usage occurrence in assembly structure.
 */
public final class StepUsageOccurrence extends AbstractStepEntity {
    private final StepEntity parent;
    private final StepEntity child;

    public StepUsageOccurrence(int id, String name, StepEntity parent, StepEntity child) {
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
