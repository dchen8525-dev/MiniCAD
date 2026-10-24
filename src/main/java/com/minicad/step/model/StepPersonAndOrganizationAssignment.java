package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal PERSON_AND_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedPersonAndOrganization assigned person and organization
 * @param role assignment role
 */
public final class StepPersonAndOrganizationAssignment extends AbstractStepEntity {
    private final StepPersonAndOrganization assignedPersonAndOrganization;
    private final StepPersonAndOrganizationRole role;

    public StepPersonAndOrganizationAssignment(int id, StepPersonAndOrganization assignedPersonAndOrganization, StepPersonAndOrganizationRole role) {
        super(id, "");
        this.assignedPersonAndOrganization = assignedPersonAndOrganization;
        this.role = role;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "assignedPersonAndOrganization", assignedPersonAndOrganization, "role", role);
    }
}
