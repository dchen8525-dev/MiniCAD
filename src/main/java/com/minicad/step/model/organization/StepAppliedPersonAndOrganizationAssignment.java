package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedPersonAndOrganization assigned person and organization
 * @param role assignment role
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedPersonAndOrganization assigned person and organization
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedPersonAndOrganizationAssignment implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepPersonAndOrganization assignedPersonAndOrganization;
    private final StepPersonAndOrganizationRole role;
    private final List<StepEntity> items;

    public StepAppliedPersonAndOrganizationAssignment(int id, String entityName, StepPersonAndOrganization assignedPersonAndOrganization, StepPersonAndOrganizationRole role, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedPersonAndOrganization = assignedPersonAndOrganization;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return entityName != null ? entityName : "";
    }

    public String getEntityName() {
        return entityName;
    }

    public StepPersonAndOrganization getAssignedPersonAndOrganization() {
        return assignedPersonAndOrganization;
    }

    public StepPersonAndOrganizationRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepPersonAndOrganization assignedPersonAndOrganization() {
        return assignedPersonAndOrganization;
    }

    public StepPersonAndOrganizationRole role() {
        return role;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedPersonAndOrganizationAssignment that = (StepAppliedPersonAndOrganizationAssignment) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedPersonAndOrganization, that.assignedPersonAndOrganization) && Objects.equals(role, that.role) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedPersonAndOrganization, role, items);
    }

    @Override
    public String toString() {
        return "StepAppliedPersonAndOrganizationAssignment{" + "id=" + id + "entityName=" + entityName + "assignedPersonAndOrganization=" + assignedPersonAndOrganization + "role=" + role + "items=" + items + "}";
    }
}
