package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_DOCUMENT_REFERENCE metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDocument assigned document
 * @param source document source label
 * @param items referenced target items
 */
public final class StepAppliedDocumentReference extends AbstractStepEntity {
    private final String entityName;
    private final StepDocument assignedDocument;
    private final String source;
    private final List<StepEntity> items;

    public StepAppliedDocumentReference(int id, String entityName, StepDocument assignedDocument, String source, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedDocument = assignedDocument;
        this.source = source;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
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

    public String source() {
        return source;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("assignedDocument", assignedDocument);
        state.put("source", source);
        state.put("items", items);
        return state;
    }
}
