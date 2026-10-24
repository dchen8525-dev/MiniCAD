package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal DOCUMENT_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingDocument source document
 * @param relatedDocument target document
 */
public final class StepDocumentRelationship extends AbstractStepEntity {
    private final String description;
    private final StepDocument relatingDocument;
    private final StepDocument relatedDocument;

    public StepDocumentRelationship(int id, String name, String description, StepDocument relatingDocument, StepDocument relatedDocument) {
        super(id, name);
        this.description = description;
        this.relatingDocument = relatingDocument;
        this.relatedDocument = relatedDocument;
    }

    public String getDescription() {
        return description;
    }

    public StepDocument getRelatingDocument() {
        return relatingDocument;
    }

    public StepDocument getRelatedDocument() {
        return relatedDocument;
    }

    // Record-style accessors
    public StepDocument relatingDocument() {
        return relatingDocument;
    }

    public StepDocument relatedDocument() {
        return relatedDocument;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description,
                "relatingDocument", relatingDocument, "relatedDocument", relatedDocument);
    }
}
