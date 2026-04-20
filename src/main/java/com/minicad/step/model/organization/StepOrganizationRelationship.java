package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal ORGANIZATION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingOrganization source organization
 * @param relatedOrganization target organization
 */
public record StepOrganizationRelationship(
        int id,
        String name,
        String description,
        StepOrganization relatingOrganization,
        StepOrganization relatedOrganization
) implements StepEntity {
}
