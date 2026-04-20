package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PERSON_AND_ORGANIZATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public record StepPersonAndOrganizationRole(
        int id,
        String name
) implements StepEntity {
}
