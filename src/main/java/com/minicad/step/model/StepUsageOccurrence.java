package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "parent", parent, "child", child);
    }
}
