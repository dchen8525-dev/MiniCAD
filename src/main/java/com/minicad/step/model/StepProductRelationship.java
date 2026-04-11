package com.minicad.step.model;

/**
 * Minimal PRODUCT_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relatingProduct source product
 * @param relatedProduct target product
 * @param entityName concrete STEP entity name
 */
public record StepProductRelationship(
        int id,
        String identifier,
        String name,
        String description,
        StepProduct relatingProduct,
        StepProduct relatedProduct,
        String entityName
) implements StepEntity {
}
