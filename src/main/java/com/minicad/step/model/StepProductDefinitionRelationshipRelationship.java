package com.minicad.step.model;

/**
 * Minimal PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relating source product definition relationship
 * @param related target product definition relationship
 * @param entityName concrete STEP entity name
 */
public record StepProductDefinitionRelationshipRelationship(
        int id,
        String identifier,
        String name,
        String description,
        StepProductDefinitionRelationship relating,
        StepProductDefinitionRelationship related,
        String entityName
) implements StepEntity {
}
