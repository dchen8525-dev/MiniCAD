package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DOCUMENT_REFERENCE metadata.
 *
 * @param id STEP instance id
 * @param assignedDocument assigned document
 * @param source document source label
 */
public final class StepDocumentReference extends AbstractStepEntity {
    private final StepDocument assignedDocument;
    private final String source;

    public StepDocumentReference(int id, StepDocument assignedDocument, String source) {
        super(id, "");
        this.assignedDocument = assignedDocument;
        this.source = source;
    }

    public String getName() {
        return source != null ? source : "";
    }

    public StepDocument getAssignedDocument() {
        return assignedDocument;
    }

    public String getSource() {
        return source;
    }

    // Record-style accessor
    public StepDocument assignedDocument() {
        return assignedDocument;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedDocument", assignedDocument);
        state.put("source", source);
        return state;
    }
}
