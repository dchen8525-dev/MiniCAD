package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedCertification assigned certification
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedCertification assigned certification
 * @param items assigned target items
 */
public final class StepAppliedCertificationAssignment implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepCertification assignedCertification;
    private final List<StepEntity> items;

    public StepAppliedCertificationAssignment(int id, String entityName, StepCertification assignedCertification, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedCertification = assignedCertification;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return entityName != null ? entityName : "";
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public StepCertification getAssignedCertification() {
        return assignedCertification;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepCertification assignedCertification() {
        return assignedCertification;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedCertificationAssignment that = (StepAppliedCertificationAssignment) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedCertification, that.assignedCertification) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedCertification, items);
    }

    @Override
    public String toString() {
        return "StepAppliedCertificationAssignment{" + "id=" + id + "entityName=" + entityName + "assignedCertification=" + assignedCertification + "items=" + items + "}";
    }
}
