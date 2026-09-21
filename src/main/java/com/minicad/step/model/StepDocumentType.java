package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DOCUMENT_TYPE metadata.
 *
 * @param id STEP instance id
 * @param productDataType document kind label
 */
public final class StepDocumentType extends AbstractStepEntity {
    private final String productDataType;

    public StepDocumentType(int id, String productDataType) {
        super(id, "");
        this.productDataType = productDataType;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("productDataType", productDataType);
        return state;
    }
}
