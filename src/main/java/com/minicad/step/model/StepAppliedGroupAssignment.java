package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_GROUP_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedGroup assigned group
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_GROUP_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedGroup assigned group
 * @param items assigned target items
 */
public final class StepAppliedGroupAssignment implements StepEntity {
    private final int id;
    private final StepGroup assignedGroup;
    private final List<StepEntity> items;

    public StepAppliedGroupAssignment(int id, StepGroup assignedGroup, List<StepEntity> items) {
        this.id = id;
        this.assignedGroup = assignedGroup;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public StepGroup getAssignedGroup() {
        return assignedGroup;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public String getName() {
        return assignedGroup != null ? assignedGroup.getName() : "";
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public StepGroup assignedGroup() {
        return assignedGroup;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedGroupAssignment that = (StepAppliedGroupAssignment) o;
        return id == that.id && Objects.equals(assignedGroup, that.assignedGroup) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedGroup, items);
    }

    @Override
    public String toString() {
        return "StepAppliedGroupAssignment{" + "id=" + id + "assignedGroup=" + assignedGroup + "items=" + items + "}";
    }
}
