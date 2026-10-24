package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved APPLIED_ATTRIBUTE_CLASSIFICATION.
 * An applied attribute classification assignment.
 */
public final class StepAppliedAttributeClassification extends AbstractStepEntity {
    private final StepEntity assignedClassification;
    private final StepEntity items;

    public StepAppliedAttributeClassification(int id, String name, StepEntity assignedClassification, StepEntity items) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "assignedClassification", assignedClassification,
                "items", items);
    }
}
