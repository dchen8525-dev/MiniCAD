package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedPersonAndOrganization", assignedPersonAndOrganization);
        state.put("role", role);
        return state;
    }
}
