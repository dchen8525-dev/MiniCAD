package com.minicad.step.model;

import java.util.Map;

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
public final class StepMakeFromRelationship extends AbstractStepEntity {
    private final String description;
    private final StepEntity relatingProduct;
    private final StepEntity relatedProduct;

    public StepMakeFromRelationship(int id, String name, String description, StepEntity relatingProduct, StepEntity relatedProduct) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description,
                "relatingProduct", relatingProduct, "relatedProduct", relatedProduct);
    }
}
