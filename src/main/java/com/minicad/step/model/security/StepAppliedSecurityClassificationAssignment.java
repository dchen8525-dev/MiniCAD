package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedSecurityClassification assigned security classification
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedSecurityClassification assigned security classification
 * @param items assigned target items
 */
public final class StepAppliedSecurityClassificationAssignment implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepSecurityClassification assignedSecurityClassification;
    private final List<StepEntity> items;

    public StepAppliedSecurityClassificationAssignment(int id, String entityName, StepSecurityClassification assignedSecurityClassification, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedSecurityClassification = assignedSecurityClassification;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public String getEntityName() {
        return entityName;
    }

    public StepSecurityClassification getAssignedSecurityClassification() {
        return assignedSecurityClassification;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepSecurityClassification assignedSecurityClassification() {
        return assignedSecurityClassification;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedSecurityClassificationAssignment that = (StepAppliedSecurityClassificationAssignment) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedSecurityClassification, that.assignedSecurityClassification) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedSecurityClassification, items);
    }

    @Override
    public String toString() {
        return "StepAppliedSecurityClassificationAssignment{" + "id=" + id + "entityName=" + entityName + "assignedSecurityClassification=" + assignedSecurityClassification + "items=" + items + "}";
    }
}
