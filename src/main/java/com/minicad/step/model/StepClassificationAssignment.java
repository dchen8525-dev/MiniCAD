package com.minicad.step.model.classification;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedClass assigned classification group
 * @param role assignment role
 */
/**
 * Minimal CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedClass assigned classification group
 * @param role assignment role
 */
public final class StepClassificationAssignment implements StepEntity {
    private final int id;
    private final StepGroup assignedClass;
    private final StepClassificationRole role;

    public StepClassificationAssignment(int id, StepGroup assignedClass, StepClassificationRole role) {
        this.id = id;
        this.assignedClass = assignedClass;
        this.role = role;
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

    // Record-style accessors
    public StepGroup assignedClass() {
        return assignedClass;
    }

    public StepClassificationRole role() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepClassificationAssignment that = (StepClassificationAssignment) o;
        return id == that.id && Objects.equals(assignedClass, that.assignedClass) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedClass, role);
    }

    @Override
    public String toString() {
        return "StepClassificationAssignment{" + "id=" + id + "assignedClass=" + assignedClass + "role=" + role + "}";
    }
}
