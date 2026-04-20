package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param identifier organization identifier
 * @param name organization name
 * @param description organization description
 */
public record StepOrganization(
        int id,
        String identifier,
        String name,
        String description
) implements StepEntity {
}
