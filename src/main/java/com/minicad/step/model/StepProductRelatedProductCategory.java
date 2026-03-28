package com.minicad.step.model;

import java.util.List;

/**
 * Minimal product category metadata.
 *
 * @param id STEP instance id
 * @param name category name
 * @param description category description
 * @param products categorized products
 */
public record StepProductRelatedProductCategory(
        int id,
        String name,
        String description,
        List<StepProduct> products
) implements StepEntity {

    public StepProductRelatedProductCategory {
        products = List.copyOf(products);
    }
}
