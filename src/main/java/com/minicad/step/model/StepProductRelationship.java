package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
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
public final class StepProductRelationship implements StepEntity {
    private final int id;
    private final String identifier;
    private final String name;
    private final String description;
    private final StepProduct relatingProduct;
    private final StepProduct relatedProduct;
    private final String entityName;

    public StepProductRelationship(int id, String identifier, String name, String description, StepProduct relatingProduct, StepProduct relatedProduct, String entityName) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.relatingProduct = relatingProduct;
        this.relatedProduct = relatedProduct;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
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
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductRelationship that = (StepProductRelationship) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingProduct, that.relatingProduct) && Objects.equals(relatedProduct, that.relatedProduct) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name, description, relatingProduct, relatedProduct, entityName);
    }

    @Override
    public String toString() {
        return "StepProductRelationship{" + "id=" + id + "identifier=" + identifier + "name=" + name + "description=" + description + "relatingProduct=" + relatingProduct + "relatedProduct=" + relatedProduct + "entityName=" + entityName + "}";
    }
}
