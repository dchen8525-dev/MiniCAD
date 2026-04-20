package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal APPLIED_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedOrganization assigned organization
 * @param role assignment role
 * @param items assigned target items
 */
public record StepAppliedOrganizationAssignment(
        int id,
        String entityName,
        StepOrganization assignedOrganization,
        StepOrganizationRole role,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedOrganizationAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return role.name();
    }
}
