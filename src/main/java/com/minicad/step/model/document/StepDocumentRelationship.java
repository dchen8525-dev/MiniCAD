package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DOCUMENT_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingDocument source document
 * @param relatedDocument target document
 */
/**
 * Minimal DOCUMENT_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingDocument source document
 * @param relatedDocument target document
 */
public final class StepDocumentRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepDocument relatingDocument;
    private final StepDocument relatedDocument;

    public StepDocumentRelationship(int id, String name, String description, StepDocument relatingDocument, StepDocument relatedDocument) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingDocument = relatingDocument;
        this.relatedDocument = relatedDocument;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDocumentRelationship that = (StepDocumentRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingDocument, that.relatingDocument) && Objects.equals(relatedDocument, that.relatedDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingDocument, relatedDocument);
    }

    @Override
    public String toString() {
        return "StepDocumentRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingDocument=" + relatingDocument + "relatedDocument=" + relatedDocument + "}";
    }
}
