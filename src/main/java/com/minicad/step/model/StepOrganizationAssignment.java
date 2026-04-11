package com.minicad.step.model;

/**
 * Minimal ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedOrganization assigned organization
 * @param role assignment role
 */
public record StepOrganizationAssignment(
        int id,
        StepOrganization assignedOrganization,
        StepOrganizationRole role
) implements StepEntity {

    @Override
    public String name() {
        return role.name();
    }
}
