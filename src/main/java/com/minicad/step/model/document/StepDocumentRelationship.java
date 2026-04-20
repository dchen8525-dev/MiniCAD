package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DOCUMENT_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingDocument source document
 * @param relatedDocument target document
 */
public record StepDocumentRelationship(
        int id,
        String name,
        String description,
        StepDocument relatingDocument,
        StepDocument relatedDocument
) implements StepEntity {
}
