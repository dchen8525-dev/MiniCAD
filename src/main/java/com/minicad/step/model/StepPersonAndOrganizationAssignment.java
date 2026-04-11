package com.minicad.step.model;

/**
 * Minimal PERSON_AND_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedPersonAndOrganization assigned person and organization
 * @param role assignment role
 */
public record StepPersonAndOrganizationAssignment(
        int id,
        StepPersonAndOrganization assignedPersonAndOrganization,
        StepPersonAndOrganizationRole role
) implements StepEntity {

    @Override
    public String name() {
        return role.name();
    }
}
