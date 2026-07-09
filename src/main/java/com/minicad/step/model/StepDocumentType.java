package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DOCUMENT_TYPE metadata.
 *
 * @param id STEP instance id
 * @param productDataType document kind label
 */
/**
 * Minimal DOCUMENT_TYPE metadata.
 *
 * @param id STEP instance id
 * @param productDataType document kind label
 */
public final class StepDocumentType implements StepEntity {
    private final int id;
    private final String productDataType;

    public StepDocumentType(int id, String productDataType) {
        this.id = id;
        this.productDataType = productDataType;
    }

    public int getId() {
        return id;
    }

    public String getProductDataType() {
        return productDataType;
    }

    public String getName() {
        return productDataType != null ? productDataType : "";
    }

    // Record-style accessor
    public String kind() {
        return productDataType;
    }

    public String productDataType() {
        return productDataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDocumentType that = (StepDocumentType) o;
        return id == that.id && Objects.equals(productDataType, that.productDataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productDataType);
    }

    @Override
    public String toString() {
        return "StepDocumentType{" + "id=" + id + "productDataType=" + productDataType + "}";
    }
}
