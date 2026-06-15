package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_DOCUMENT_REFERENCE metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDocument assigned document
 * @param source document source label
 * @param items referenced target items
 */
/**
 * Minimal APPLIED_DOCUMENT_REFERENCE metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDocument assigned document
 * @param source document source label
 * @param items referenced target items
 */
public final class StepAppliedDocumentReference implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepDocument assignedDocument;
    private final String source;
    private final List<StepEntity> items;

    public StepAppliedDocumentReference(int id, String entityName, StepDocument assignedDocument, String source, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedDocument = assignedDocument;
        this.source = source;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }

    public StepDocument getAssignedDocument() {
        return assignedDocument;
    }

    public String getSource() {
        return source;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public String getName() {
        return entityName != null ? entityName : "";
    }

    // Record-style accessors
    public StepDocument assignedDocument() {
        return assignedDocument;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedDocumentReference that = (StepAppliedDocumentReference) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedDocument, that.assignedDocument) && Objects.equals(source, that.source) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedDocument, source, items);
    }

    @Override
    public String toString() {
        return "StepAppliedDocumentReference{" + "id=" + id + "entityName=" + entityName + "assignedDocument=" + assignedDocument + "source=" + source + "items=" + items + "}";
    }
}
