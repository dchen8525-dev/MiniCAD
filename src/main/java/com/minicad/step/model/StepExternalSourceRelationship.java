package com.minicad.step.model;

/**
 * Minimal EXTERNAL_SOURCE_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingSource relating source
 * @param relatedSource related source
 */
public record StepExternalSourceRelationship(
        int id,
        String name,
        String description,
        StepExternalSource relatingSource,
        StepExternalSource relatedSource
) implements StepEntity {
}
