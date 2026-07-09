package com.minicad.step.model.classification;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal GROUP_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedGroup assigned group
 */
/**
 * Minimal GROUP_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedGroup assigned group
 */
public final class StepGroupAssignment implements StepEntity {
    private final int id;
    private final StepGroup assignedGroup;

    public StepGroupAssignment(int id, StepGroup assignedGroup) {
        this.id = id;
        this.assignedGroup = assignedGroup;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepGroup getAssignedGroup() {
        return assignedGroup;
    }

    // Record-style accessor
    public StepGroup assignedGroup() {
        return assignedGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGroupAssignment that = (StepGroupAssignment) o;
        return id == that.id && Objects.equals(assignedGroup, that.assignedGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedGroup);
    }

    @Override
    public String toString() {
        return "StepGroupAssignment{" + "id=" + id + "assignedGroup=" + assignedGroup + "}";
    }
}
