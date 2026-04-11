package com.minicad.step.model;

/**
 * Minimal PRODUCT_CATEGORY metadata.
 *
 * @param id STEP instance id
 * @param name category name
 * @param description category description
 */
public record StepProductCategory(
        int id,
        String name,
        String description
) implements StepEntity {
}
