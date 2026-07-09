package com.minicad.step.model.organization.org.org;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedOrganization assigned organization
 * @param role assignment role
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedOrganization assigned organization
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedOrganizationAssignment implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepOrganization assignedOrganization;
    private final StepOrganizationRole role;
    private final List<StepEntity> items;

    public StepAppliedOrganizationAssignment(int id, String entityName, StepOrganization assignedOrganization, StepOrganizationRole role, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedOrganization = assignedOrganization;
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

    public String entityName() {
        return entityName;
    }

    public StepOrganization getAssignedOrganization() {
        return assignedOrganization;
    }

    public StepOrganizationRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepOrganization assignedOrganization() {
        return assignedOrganization;
    }

    public StepOrganizationRole role() {
        return role;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedOrganizationAssignment that = (StepAppliedOrganizationAssignment) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedOrganization, that.assignedOrganization) && Objects.equals(role, that.role) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedOrganization, role, items);
    }

    @Override
    public String toString() {
        return "StepAppliedOrganizationAssignment{" + "id=" + id + "entityName=" + entityName + "assignedOrganization=" + assignedOrganization + "role=" + role + "items=" + items + "}";
    }
}
