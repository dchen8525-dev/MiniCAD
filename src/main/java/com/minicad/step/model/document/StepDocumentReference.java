package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DOCUMENT_REFERENCE metadata.
 *
 * @param id STEP instance id
 * @param assignedDocument assigned document
 * @param source document source label
 */
/**
 * Minimal DOCUMENT_REFERENCE metadata.
 *
 * @param id STEP instance id
 * @param assignedDocument assigned document
 * @param source document source label
 */
public final class StepDocumentReference implements StepEntity {
    private final int id;
    private final StepDocument assignedDocument;
    private final String source;

    public StepDocumentReference(int id, StepDocument assignedDocument, String source) {
        this.id = id;
        this.assignedDocument = assignedDocument;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public StepDocument getAssignedDocument() {
        return assignedDocument;
    }

    public String getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDocumentReference that = (StepDocumentReference) o;
        return id == that.id && Objects.equals(assignedDocument, that.assignedDocument) && Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedDocument, source);
    }

    @Override
    public String toString() {
        return "StepDocumentReference{" + "id=" + id + "assignedDocument=" + assignedDocument + "source=" + source + "}";
    }
}
