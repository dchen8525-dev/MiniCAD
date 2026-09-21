package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal product category metadata.
 *
 * @param id STEP instance id
 * @param name category name
 * @param description category description
 * @param products categorized products
 */
public final class StepProductRelatedProductCategory extends AbstractStepEntity {
    private final String description;
    private final List<StepProduct> products;

    public StepProductRelatedProductCategory(int id, String name, String description, List<StepProduct> products) {
        super(id, name);
        this.description = description;
        this.products = products == null ? null : java.util.List.copyOf(products);
    }

    public String getDescription() {
        return description;
    }

    public List<StepProduct> getProducts() {
        return products;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public String description() {
        return description;
    }

    public List<StepProduct> products() {
        return products;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("products", products);
        return state;
    }
}
