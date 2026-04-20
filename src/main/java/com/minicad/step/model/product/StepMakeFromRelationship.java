package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved MAKE_FROM_RELATIONSHIP.
 * Relationship indicating a product is made from another.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingProduct source product
 * @param relatedProduct resulting product
 */
public record StepMakeFromRelationship(
    int id,
    String name,
    String description,
    StepEntity relatingProduct,
    StepEntity relatedProduct) implements StepEntity {
}
