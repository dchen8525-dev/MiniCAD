package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ATTRIBUTE_CLASSIFICATION.
 * An attribute classification assignment.
 */
public final class StepAttributeClassification extends AbstractStepEntity {
    private final StepEntity assignedClassification;
    private final StepEntity items;

    public StepAttributeClassification(int id, String name, StepEntity assignedClassification, StepEntity items) {
        super(id, name);
        this.assignedClassification = assignedClassification;
        this.items = items;
    }

    public StepEntity getAssignedClassification() {
        return assignedClassification;
    }

    public StepEntity getItems() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("assignedClassification", assignedClassification);
        state.put("items", items);
        return state;
    }
}
