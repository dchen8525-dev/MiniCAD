package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal APPROVAL_PERSON_ORGANIZATION assignment.
 *
 * @param id STEP instance id
 * @param personOrganization assigned person and organization
 * @param authorizedApproval approval
 * @param role approval role
 */
public final class StepApprovalPersonOrganization extends AbstractStepEntity {
    private final StepPersonAndOrganization personOrganization;
    private final StepApproval authorizedApproval;
    private final StepApprovalRole role;

    public StepApprovalPersonOrganization(int id, StepPersonAndOrganization personOrganization, StepApproval authorizedApproval, StepApprovalRole role) {
        super(id, "");
        this.personOrganization = personOrganization;
        this.authorizedApproval = authorizedApproval;
        this.role = role;
    }

    public StepPersonAndOrganization getPersonOrganization() {
        return personOrganization;
    }

    public StepApproval getAuthorizedApproval() {
        return authorizedApproval;
    }

    public StepApprovalRole getRole() {
        return role;
    }

    // Record-style accessors
    public StepPersonAndOrganization personOrganization() {
        return personOrganization;
    }

    public StepApproval authorizedApproval() {
        return authorizedApproval;
    }

    public StepApprovalRole role() {
        return role;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("personOrganization", personOrganization);
        state.put("authorizedApproval", authorizedApproval);
        state.put("role", role);
        return state;
    }
}
