package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved EXCLUSION_ASSIGNMENT.
 */
/**
 * Resolved EXCLUSION_ASSIGNMENT.
 */
public final class StepExclusionAssignment implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> assignedItems;
    private final StepEntity role;

    public StepExclusionAssignment(int id, String name, List<StepEntity> assignedItems, StepEntity role) {
        this.id = id;
        this.name = name;
        this.assignedItems = assignedItems == null ? null : java.util.List.copyOf(assignedItems);
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getAssignedItems() {
        return assignedItems;
    }

    public StepEntity getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExclusionAssignment that = (StepExclusionAssignment) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(assignedItems, that.assignedItems) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assignedItems, role);
    }

    @Override
    public String toString() {
        return "StepExclusionAssignment{" + "id=" + id + "name=" + name + "assignedItems=" + assignedItems + "role=" + role + "}";
    }
}
