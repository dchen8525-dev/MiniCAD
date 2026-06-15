package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_NAME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedName assigned name
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_NAME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedName assigned name
 * @param items assigned target items
 */
public final class StepAppliedNameAssignment implements StepEntity {
    private final int id;
    private final String assignedName;
    private final List<StepEntity> items;

    public StepAppliedNameAssignment(int id, String assignedName, List<StepEntity> items) {
        this.id = id;
        this.assignedName = assignedName;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return assignedName != null ? assignedName : "";
    }

    public String getAssignedName() {
        return assignedName;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public String namedItem() {
        return assignedName;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedNameAssignment that = (StepAppliedNameAssignment) o;
        return id == that.id && Objects.equals(assignedName, that.assignedName) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedName, items);
    }

    @Override
    public String toString() {
        return "StepAppliedNameAssignment{" + "id=" + id + "assignedName=" + assignedName + "items=" + items + "}";
    }
}
