package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedOrganization assigned organization
 * @param role assignment role
 */
public final class StepOrganizationAssignment extends AbstractStepEntity {
    private final StepOrganization assignedOrganization;
    private final StepOrganizationRole role;

    public StepOrganizationAssignment(int id, StepOrganization assignedOrganization, StepOrganizationRole role) {
        super(id, "");
        this.assignedOrganization = assignedOrganization;
        this.role = role;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedOrganization", assignedOrganization);
        state.put("role", role);
        return state;
    }
}
