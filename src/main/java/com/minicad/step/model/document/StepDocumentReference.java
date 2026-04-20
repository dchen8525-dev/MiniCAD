package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DOCUMENT_REFERENCE metadata.
 *
 * @param id STEP instance id
 * @param assignedDocument assigned document
 * @param source document source label
 */
public record StepDocumentReference(
        int id,
        StepDocument assignedDocument,
        String source
) implements StepEntity {

    @Override
    public String name() {
        return assignedDocument.name();
    }
}
