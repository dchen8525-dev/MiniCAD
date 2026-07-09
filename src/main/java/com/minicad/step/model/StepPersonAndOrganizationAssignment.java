package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PERSON_AND_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedPersonAndOrganization assigned person and organization
 * @param role assignment role
 */
/**
 * Minimal PERSON_AND_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedPersonAndOrganization assigned person and organization
 * @param role assignment role
 */
public final class StepPersonAndOrganizationAssignment implements StepEntity {
    private final int id;
    private final StepPersonAndOrganization assignedPersonAndOrganization;
    private final StepPersonAndOrganizationRole role;

    public StepPersonAndOrganizationAssignment(int id, StepPersonAndOrganization assignedPersonAndOrganization, StepPersonAndOrganizationRole role) {
        this.id = id;
        this.assignedPersonAndOrganization = assignedPersonAndOrganization;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepPersonAndOrganization getAssignedPersonAndOrganization() {
        return assignedPersonAndOrganization;
    }

    public StepPersonAndOrganizationRole getRole() {
        return role;
    }

    // Record-style accessors
    public StepPersonAndOrganization assignedPersonAndOrganization() {
        return assignedPersonAndOrganization;
    }

    public StepPersonAndOrganizationRole role() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPersonAndOrganizationAssignment that = (StepPersonAndOrganizationAssignment) o;
        return id == that.id && Objects.equals(assignedPersonAndOrganization, that.assignedPersonAndOrganization) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedPersonAndOrganization, role);
    }

    @Override
    public String toString() {
        return "StepPersonAndOrganizationAssignment{" + "id=" + id + "assignedPersonAndOrganization=" + assignedPersonAndOrganization + "role=" + role + "}";
    }
}
