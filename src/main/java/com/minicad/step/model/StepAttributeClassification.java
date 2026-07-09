package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved ATTRIBUTE_CLASSIFICATION.
 * An attribute classification assignment.
 */
/**
 * Resolved ATTRIBUTE_CLASSIFICATION.
 * An attribute classification assignment.
 */
public final class StepAttributeClassification implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity assignedClassification;
    private final StepEntity items;

    public StepAttributeClassification(int id, String name, StepEntity assignedClassification, StepEntity items) {
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
        StepAttributeClassification that = (StepAttributeClassification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(assignedClassification, that.assignedClassification) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assignedClassification, items);
    }

    @Override
    public String toString() {
        return "StepAttributeClassification{" + "id=" + id + "name=" + name + "assignedClassification=" + assignedClassification + "items=" + items + "}";
    }
}
