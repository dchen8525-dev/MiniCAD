package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.organization.StepPersonAndOrganization;
/**
 * Minimal APPROVAL_PERSON_ORGANIZATION assignment.
 *
 * @param id STEP instance id
 * @param personOrganization assigned person and organization
 * @param authorizedApproval approval
 * @param role approval role
 */
public record StepApprovalPersonOrganization(
        int id,
        StepPersonAndOrganization personOrganization,
        StepApproval authorizedApproval,
        StepApprovalRole role
) implements StepEntity {

    @Override
    public String name() {
        return role.name();
    }
}
