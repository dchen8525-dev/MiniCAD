package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved APPLIED_ATTRIBUTE_CLASSIFICATION.
 * An applied attribute classification assignment.
 */
/**
 * Resolved APPLIED_ATTRIBUTE_CLASSIFICATION.
 * An applied attribute classification assignment.
 */
public final class StepAppliedAttributeClassification implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity assignedClassification;
    private final StepEntity items;

    public StepAppliedAttributeClassification(int id, String name, StepEntity assignedClassification, StepEntity items) {
        this.id = id;
        this.name = name;
        this.assignedClassification = assignedClassification;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getAssignedClassification() {
        return assignedClassification;
    }

    public StepEntity getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedAttributeClassification that = (StepAppliedAttributeClassification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(assignedClassification, that.assignedClassification) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assignedClassification, items);
    }

    @Override
    public String toString() {
        return "StepAppliedAttributeClassification{" + "id=" + id + "name=" + name + "assignedClassification=" + assignedClassification + "items=" + items + "}";
    }
}
