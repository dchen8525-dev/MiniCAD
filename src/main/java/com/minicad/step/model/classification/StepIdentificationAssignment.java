package com.minicad.step.model.classification;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role assignment role
 */
/**
 * Minimal IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role assignment role
 */
public final class StepIdentificationAssignment implements StepEntity {
    private final int id;
    private final String assignedId;
    private final StepIdentificationRole role;

    public StepIdentificationAssignment(int id, String assignedId, StepIdentificationRole role) {
        this.id = id;
        this.assignedId = assignedId;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public StepIdentificationRole getRole() {
        return role;
    }

    // Record-style accessors
    public int id() { return id; }
    public String getName() { return ""; }
    public String assignedId() { return assignedId; }
    public StepIdentificationRole role() { return role; }
    public String identifiedItem() { return assignedId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIdentificationAssignment that = (StepIdentificationAssignment) o;
        return id == that.id && Objects.equals(assignedId, that.assignedId) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedId, role);
    }

    @Override
    public String toString() {
        return "StepIdentificationAssignment{" + "id=" + id + "assignedId=" + assignedId + "role=" + role + "}";
    }
}
