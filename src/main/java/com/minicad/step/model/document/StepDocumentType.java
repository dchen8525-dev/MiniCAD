package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DOCUMENT_TYPE metadata.
 *
 * @param id STEP instance id
 * @param productDataType document kind label
 */
public record StepDocumentType(
        int id,
        String productDataType
) implements StepEntity {

    @Override
    public String name() {
        return productDataType;
    }
}
