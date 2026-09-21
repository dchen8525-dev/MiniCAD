package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS.
 * Product definition with linked documents.
 */
public final class StepProductDefinitionWithAssociatedDocuments extends AbstractStepEntity {
    private final String description;
    private final List<StepEntity> documents;

    public StepProductDefinitionWithAssociatedDocuments(int id, String name, String description, List<StepEntity> documents) {
        super(id, name);
        this.description = description;
        this.documents = documents == null ? null : java.util.List.copyOf(documents);
    }

    public String getDescription() {
        return description;
    }

    public List<StepEntity> getDocuments() {
        return documents;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("documents", documents);
        return state;
    }
}
