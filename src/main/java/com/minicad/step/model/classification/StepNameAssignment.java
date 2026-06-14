package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal NAME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedName assigned name
 */
/**
 * Minimal NAME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedName assigned name
 */
public final class StepNameAssignment implements StepEntity {
    private final int id;
    private final String assignedName;

    public StepNameAssignment(int id, String assignedName) {
        this.id = id;
        this.assignedName = assignedName;
    }

    public int getId() {
        return id;
    }

    public String getAssignedName() {
        return assignedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNameAssignment that = (StepNameAssignment) o;
        return id == that.id && Objects.equals(assignedName, that.assignedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedName);
    }

    @Override
    public String toString() {
        return "StepNameAssignment{" + "id=" + id + "assignedName=" + assignedName + "}";
    }
}
