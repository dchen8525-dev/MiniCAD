package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ALTERNATE_PRODUCT_RELATIONSHIP.
 * Alternate product relationship.
 */
public final class StepAlternateProductRelationship extends AbstractStepEntity {
    private final String description;
    private final StepEntity relatingProduct;
    private final StepEntity relatedProduct;

    public StepAlternateProductRelationship(int id, String name, String description, StepEntity relatingProduct, StepEntity relatedProduct) {
        super(id, name);
        this.description = description;
        this.relatingProduct = relatingProduct;
        this.relatedProduct = relatedProduct;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getRelatingProduct() {
        return relatingProduct;
    }

    public StepEntity getRelatedProduct() {
        return relatedProduct;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingProduct", relatingProduct);
        state.put("relatedProduct", relatedProduct);
        return state;
    }
}
