package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal GENERAL_PROPERTY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingGeneralProperty relating property
 * @param relatedGeneralProperty related property
 */
public record StepGeneralPropertyRelationship(
        int id,
        String name,
        String description,
        StepGeneralProperty relatingGeneralProperty,
        StepGeneralProperty relatedGeneralProperty
) implements StepEntity {
}
