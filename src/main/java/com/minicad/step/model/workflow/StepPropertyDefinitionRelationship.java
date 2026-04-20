package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PROPERTY_DEFINITION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingPropertyDefinition source property definition
 * @param relatedPropertyDefinition target property definition
 * @param entityName concrete STEP entity name
 */
public record StepPropertyDefinitionRelationship(
        int id,
        String name,
        String description,
        StepPropertyDefinition relatingPropertyDefinition,
        StepPropertyDefinition relatedPropertyDefinition,
        String entityName
) implements StepEntity {
}
