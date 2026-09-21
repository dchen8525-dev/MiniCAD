package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal GROUP_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingGroup source group
 * @param relatedGroup target group
 * @param entityName concrete STEP entity name
 */
public final class StepGroupRelationship extends AbstractStepEntity {
    private final String description;
    private final StepGroup relatingGroup;
    private final StepGroup relatedGroup;
    private final String entityName;

    public StepGroupRelationship(int id, String name, String description, StepGroup relatingGroup, StepGroup relatedGroup, String entityName) {
        super(id, name);
        this.description = description;
        this.relatingGroup = relatingGroup;
        this.relatedGroup = relatedGroup;
        this.entityName = entityName;
    }

    public String getDescription() {
        return description;
    }

    public StepGroup getRelatingGroup() {
        return relatingGroup;
    }

    public StepGroup getRelatedGroup() {
        return relatedGroup;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public StepGroup relatingGroup() {
        return relatingGroup;
    }

    public StepGroup relatedGroup() {
        return relatedGroup;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingGroup", relatingGroup);
        state.put("relatedGroup", relatedGroup);
        state.put("entityName", entityName);
        return state;
    }
}
