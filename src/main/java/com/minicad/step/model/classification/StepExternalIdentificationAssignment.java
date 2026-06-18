package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal EXTERNAL_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role identification role
 * @param source external source
 */
/**
 * Minimal EXTERNAL_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role identification role
 * @param source external source
 */
public final class StepExternalIdentificationAssignment implements StepEntity {
    private final int id;
    private final String assignedId;
    private final StepIdentificationRole role;
    private final StepExternalSource source;

    public StepExternalIdentificationAssignment(int id, String assignedId, StepIdentificationRole role, StepExternalSource source) {
        this.id = id;
        this.assignedId = assignedId;
        this.role = role;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return assignedId != null ? assignedId : "";
    }

    public String getAssignedId() {
        return assignedId;
    }

    public StepIdentificationRole getRole() {
        return role;
    }

    public StepExternalSource getSource() {
        return source;
    }

    // Record-style accessors
    public String assignedId() {
        return assignedId;
    }

    public StepIdentificationRole role() {
        return role;
    }

    public StepExternalSource source() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExternalIdentificationAssignment that = (StepExternalIdentificationAssignment) o;
        return id == that.id && Objects.equals(assignedId, that.assignedId) && Objects.equals(role, that.role) && Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedId, role, source);
    }

    @Override
    public String toString() {
        return "StepExternalIdentificationAssignment{" + "id=" + id + "assignedId=" + assignedId + "role=" + role + "source=" + source + "}";
    }
}
