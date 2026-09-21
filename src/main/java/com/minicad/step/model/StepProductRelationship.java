package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepProductRelationship extends AbstractStepEntity {
    private final String identifier;
    private final String description;
    private final StepProduct relatingProduct;
    private final StepProduct relatedProduct;
    private final String entityName;

    public StepProductRelationship(int id, String identifier, String name, String description, StepProduct relatingProduct, StepProduct relatedProduct, String entityName) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
        this.relatingProduct = relatingProduct;
        this.relatedProduct = relatedProduct;
        this.entityName = entityName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public StepProduct getRelatingProduct() {
        return relatingProduct;
    }

    public StepProduct getRelatedProduct() {
        return relatedProduct;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public String identifier() {
        return identifier;
    }

    public String name() {
        return getName();
    }

    public String description() {
        return description;
    }

    public StepProduct relatingProduct() {
        return relatingProduct;
    }

    public StepProduct relatedProduct() {
        return relatedProduct;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("identifier", identifier);
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingProduct", relatingProduct);
        state.put("relatedProduct", relatedProduct);
        state.put("entityName", entityName);
        return state;
    }
}
