package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal layer assignment.
 *
 * @param id STEP instance id
 * @param name layer name
 * @param description optional layer description
 * @param assignedItems assigned STEP items
 */
/**
 * Minimal layer assignment.
 *
 * @param id STEP instance id
 * @param name layer name
 * @param description optional layer description
 * @param assignedItems assigned STEP items
 */
public final class StepPresentationLayerAssignment implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final List<StepEntity> assignedItems;

    public StepPresentationLayerAssignment(int id, String name, String description, List<StepEntity> assignedItems) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.assignedItems = assignedItems == null ? null : java.util.List.copyOf(assignedItems);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<StepEntity> getAssignedItems() {
        return assignedItems;
    }

    // Record-style accessor
    public List<StepEntity> assignedItems() {
        return assignedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPresentationLayerAssignment that = (StepPresentationLayerAssignment) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(assignedItems, that.assignedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, assignedItems);
    }

    @Override
    public String toString() {
        return "StepPresentationLayerAssignment{" + "id=" + id + "name=" + name + "description=" + description + "assignedItems=" + assignedItems + "}";
    }
}
