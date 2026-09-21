package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal EXTERNAL_SOURCE_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingSource relating source
 * @param relatedSource related source
 */
public final class StepExternalSourceRelationship extends AbstractStepEntity {
    private final String description;
    private final StepExternalSource relatingSource;
    private final StepExternalSource relatedSource;

    public StepExternalSourceRelationship(int id, String name, String description, StepExternalSource relatingSource, StepExternalSource relatedSource) {
        super(id, name);
        this.description = description;
        this.relatingSource = relatingSource;
        this.relatedSource = relatedSource;
    }

    public String getDescription() {
        return description;
    }

    public StepExternalSource getRelatingSource() {
        return relatingSource;
    }

    public StepExternalSource getRelatedSource() {
        return relatedSource;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public String description() {
        return description;
    }

    public StepExternalSource relatingSource() {
        return relatingSource;
    }

    public StepExternalSource relatedSource() {
        return relatedSource;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingSource", relatingSource);
        state.put("relatedSource", relatedSource);
        return state;
    }
}
