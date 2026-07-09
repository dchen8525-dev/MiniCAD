package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LAYERED_ITEM.
 * An item assigned to presentation layers.
 *
 * @param id STEP instance id
 * @param name item name
 * @param assignment layers assignment reference
 */
/**
 * Resolved LAYERED_ITEM.
 * An item assigned to presentation layers.
 *
 * @param id STEP instance id
 * @param name item name
 * @param assignment layers assignment reference
 */
public final class StepLayeredItem implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity assignment;

    public StepLayeredItem(int id, String name, StepEntity assignment) {
        this.id = id;
        this.name = name;
        this.assignment = assignment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getAssignment() {
        return assignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLayeredItem that = (StepLayeredItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(assignment, that.assignment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assignment);
    }

    @Override
    public String toString() {
        return "StepLayeredItem{" + "id=" + id + "name=" + name + "assignment=" + assignment + "}";
    }
}
