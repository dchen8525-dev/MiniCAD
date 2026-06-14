package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.organization.StepPersonAndOrganization;
import java.util.Objects;
/**
 * Minimal APPROVAL_PERSON_ORGANIZATION assignment.
 *
 * @param id STEP instance id
 * @param personOrganization assigned person and organization
 * @param authorizedApproval approval
 * @param role approval role
 */
/**
 * Minimal APPROVAL_PERSON_ORGANIZATION assignment.
 *
 * @param id STEP instance id
 * @param personOrganization assigned person and organization
 * @param authorizedApproval approval
 * @param role approval role
 */
public final class StepApprovalPersonOrganization implements StepEntity {
    private final int id;
    private final StepPersonAndOrganization personOrganization;
    private final StepApproval authorizedApproval;
    private final StepApprovalRole role;

    public StepApprovalPersonOrganization(int id, StepPersonAndOrganization personOrganization, StepApproval authorizedApproval, StepApprovalRole role) {
        this.id = id;
        this.personOrganization = personOrganization;
        this.authorizedApproval = authorizedApproval;
        this.role = role;
    }

    public int getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApprovalPersonOrganization that = (StepApprovalPersonOrganization) o;
        return id == that.id && Objects.equals(personOrganization, that.personOrganization) && Objects.equals(authorizedApproval, that.authorizedApproval) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personOrganization, authorizedApproval, role);
    }

    @Override
    public String toString() {
        return "StepApprovalPersonOrganization{" + "id=" + id + "personOrganization=" + personOrganization + "authorizedApproval=" + authorizedApproval + "role=" + role + "}";
    }
}
