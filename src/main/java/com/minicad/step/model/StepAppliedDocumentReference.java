package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_DOCUMENT_REFERENCE metadata.
 *
 * @param id STEP instance id
 * @param assignedDocument assigned document
 * @param source document source label
 * @param items referenced target items
 */
public record StepAppliedDocumentReference(
        int id,
        StepDocument assignedDocument,
        String source,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedDocumentReference {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedDocument.name();
    }
}
