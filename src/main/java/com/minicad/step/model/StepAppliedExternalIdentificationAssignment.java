package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role identification role
 * @param source external source
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role identification role
 * @param source external source
 * @param items assigned target items
 */
public final class StepAppliedExternalIdentificationAssignment implements StepEntity {
    private final int id;
    private final String assignedId;
    private final StepIdentificationRole role;
    private final StepExternalSource source;
    private final List<StepEntity> items;

    public StepAppliedExternalIdentificationAssignment(int id, String assignedId, StepIdentificationRole role, StepExternalSource source, List<StepEntity> items) {
        this.id = id;
        this.assignedId = assignedId;
        this.role = role;
        this.source = source;
        this.items = items == null ? null : java.util.List.copyOf(items);
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

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepIdentificationRole role() {
        return role;
    }

    public StepExternalSource source() {
        return source;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedExternalIdentificationAssignment that = (StepAppliedExternalIdentificationAssignment) o;
        return id == that.id && Objects.equals(assignedId, that.assignedId) && Objects.equals(role, that.role) && Objects.equals(source, that.source) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedId, role, source, items);
    }

    @Override
    public String toString() {
        return "StepAppliedExternalIdentificationAssignment{" + "id=" + id + "assignedId=" + assignedId + "role=" + role + "source=" + source + "items=" + items + "}";
    }
}
