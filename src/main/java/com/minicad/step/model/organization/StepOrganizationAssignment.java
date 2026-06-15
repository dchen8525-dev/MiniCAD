package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedOrganization assigned organization
 * @param role assignment role
 */
/**
 * Minimal ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedOrganization assigned organization
 * @param role assignment role
 */
public final class StepOrganizationAssignment implements StepEntity {
    private final int id;
    private final StepOrganization assignedOrganization;
    private final StepOrganizationRole role;

    public StepOrganizationAssignment(int id, StepOrganization assignedOrganization, StepOrganizationRole role) {
        this.id = id;
        this.assignedOrganization = assignedOrganization;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepOrganization getAssignedOrganization() {
        return assignedOrganization;
    }

    public StepOrganizationRole getRole() {
        return role;
    }

    // Record-style accessors
    public StepOrganization assignedOrganization() {
        return assignedOrganization;
    }

    public StepOrganizationRole role() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrganizationAssignment that = (StepOrganizationAssignment) o;
        return id == that.id && Objects.equals(assignedOrganization, that.assignedOrganization) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedOrganization, role);
    }

    @Override
    public String toString() {
        return "StepOrganizationAssignment{" + "id=" + id + "assignedOrganization=" + assignedOrganization + "role=" + role + "}";
    }
}
