package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role assignment role
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedIdentificationAssignment implements StepEntity {
    private final int id;
    private final String assignedId;
    private final StepIdentificationRole role;
    private final List<StepEntity> items;

    public StepAppliedIdentificationAssignment(int id, String assignedId, StepIdentificationRole role, List<StepEntity> items) {
        this.id = id;
        this.assignedId = assignedId;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public String getName() {
        return assignedId != null ? assignedId : "";
    }

    // Record-style accessor - name from assignedId
    public String name() {
        return assignedId;
    }

    public StepIdentificationRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepIdentificationRole role() {
        return role;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedIdentificationAssignment that = (StepAppliedIdentificationAssignment) o;
        return id == that.id && Objects.equals(assignedId, that.assignedId) && Objects.equals(role, that.role) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedId, role, items);
    }

    @Override
    public String toString() {
        return "StepAppliedIdentificationAssignment{" + "id=" + id + "assignedId=" + assignedId + "role=" + role + "items=" + items + "}";
    }
}
