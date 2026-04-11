package com.minicad.step.model;

/**
 * Minimal PRODUCT_DEFINITION_FORMATION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relatingFormation source formation
 * @param relatedFormation target formation
 */
public record StepProductDefinitionFormationRelationship(
        int id,
        String identifier,
        String name,
        String description,
        StepProductDefinitionFormation relatingFormation,
        StepProductDefinitionFormation relatedFormation
) implements StepEntity {
}
