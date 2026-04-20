package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRODUCT_CATEGORY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param category parent category
 * @param subCategory child category
 */
public record StepProductCategoryRelationship(
        int id,
        String name,
        String description,
        StepProductCategory category,
        StepProductCategory subCategory
) implements StepEntity {
}
