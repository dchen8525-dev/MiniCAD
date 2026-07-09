package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ALTERNATE_PRODUCT_RELATIONSHIP.
 * Alternate product relationship.
 */
/**
 * Resolved ALTERNATE_PRODUCT_RELATIONSHIP.
 * Alternate product relationship.
 */
public final class StepAlternateProductRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity relatingProduct;
    private final StepEntity relatedProduct;

    public StepAlternateProductRelationship(int id, String name, String description, StepEntity relatingProduct, StepEntity relatedProduct) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingProduct = relatingProduct;
        this.relatedProduct = relatedProduct;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAlternateProductRelationship that = (StepAlternateProductRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingProduct, that.relatingProduct) && Objects.equals(relatedProduct, that.relatedProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingProduct, relatedProduct);
    }

    @Override
    public String toString() {
        return "StepAlternateProductRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingProduct=" + relatingProduct + "relatedProduct=" + relatedProduct + "}";
    }
}
