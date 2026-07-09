package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_APPROVAL_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedApproval assigned approval
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_APPROVAL_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedApproval assigned approval
 * @param items assigned target items
 */
public final class StepAppliedApprovalAssignment implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepApproval assignedApproval;
    private final List<StepEntity> items;

    public StepAppliedApprovalAssignment(int id, String entityName, StepApproval assignedApproval, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedApproval = assignedApproval;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public StepApproval getAssignedApproval() {
        return assignedApproval;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepApproval assignedApproval() {
        return assignedApproval;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedApprovalAssignment that = (StepAppliedApprovalAssignment) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedApproval, that.assignedApproval) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedApproval, items);
    }

    @Override
    public String toString() {
        return "StepAppliedApprovalAssignment{" + "id=" + id + "entityName=" + entityName + "assignedApproval=" + assignedApproval + "items=" + items + "}";
    }
}
