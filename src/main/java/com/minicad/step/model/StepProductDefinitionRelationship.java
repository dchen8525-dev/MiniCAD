package com.minicad.step.model;

/**
 * Minimal PRODUCT_DEFINITION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relatingProductDefinition source product definition
 * @param relatedProductDefinition target product definition
 * @param entityName concrete STEP entity name
 */
public record StepProductDefinitionRelationship(
        int id,
        String identifier,
        String name,
        String description,
        StepProductDefinition relatingProductDefinition,
        StepProductDefinition relatedProductDefinition,
        String entityName
) implements StepEntity {
}
