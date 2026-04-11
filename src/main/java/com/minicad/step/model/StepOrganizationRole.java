package com.minicad.step.model;

/**
 * Minimal ORGANIZATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public record StepOrganizationRole(
        int id,
        String name
) implements StepEntity {
}
