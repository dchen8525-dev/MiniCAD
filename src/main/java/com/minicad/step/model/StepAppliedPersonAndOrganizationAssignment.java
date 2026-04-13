package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedPersonAndOrganization assigned person and organization
 * @param role assignment role
 * @param items assigned target items
 */
public record StepAppliedPersonAndOrganizationAssignment(
        int id,
        String entityName,
        StepPersonAndOrganization assignedPersonAndOrganization,
        StepPersonAndOrganizationRole role,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedPersonAndOrganizationAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return role.name();
    }
}
