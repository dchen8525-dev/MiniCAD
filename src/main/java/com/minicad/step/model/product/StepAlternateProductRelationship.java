package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
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
