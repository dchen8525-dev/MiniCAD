package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedClass assigned classification group
 * @param role assignment role
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedClass assigned classification group
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedClassificationAssignment implements StepEntity {
    private final int id;
    private final StepGroup assignedClass;
    private final StepClassificationRole role;
    private final List<StepEntity> items;

    public StepAppliedClassificationAssignment(int id, StepGroup assignedClass, StepClassificationRole role, List<StepEntity> items) {
        this.id = id;
        this.assignedClass = assignedClass;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepGroup getAssignedClass() {
        return assignedClass;
    }

    public StepClassificationRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepGroup assignedClass() {
        return assignedClass;
    }

    public StepClassificationRole role() {
        return role;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedClassificationAssignment that = (StepAppliedClassificationAssignment) o;
        return id == that.id && Objects.equals(assignedClass, that.assignedClass) && Objects.equals(role, that.role) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedClass, role, items);
    }

    @Override
    public String toString() {
        return "StepAppliedClassificationAssignment{" + "id=" + id + "assignedClass=" + assignedClass + "role=" + role + "items=" + items + "}";
    }
}
