package com.minicad.step.model;

/**
 * Resolved ALTERNATE_PRODUCT_RELATIONSHIP.
 * Alternate product relationship.
 */
public record StepAlternateProductRelationship(
    int id,
    String name,
    String description,
    StepEntity relatingProduct,
    StepEntity relatedProduct) implements StepEntity {
}
