package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PROPERTY_DEFINITION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingPropertyDefinition source property definition
 * @param relatedPropertyDefinition target property definition
 * @param entityName concrete STEP entity name
 */
public final class StepPropertyDefinitionRelationship extends AbstractStepEntity {
    private final String description;
    private final StepPropertyDefinition relatingPropertyDefinition;
    private final StepPropertyDefinition relatedPropertyDefinition;
    private final String entityName;

    public StepPropertyDefinitionRelationship(int id, String name, String description, StepPropertyDefinition relatingPropertyDefinition, StepPropertyDefinition relatedPropertyDefinition, String entityName) {
        super(id, name);
        this.description = description;
        this.relatingPropertyDefinition = relatingPropertyDefinition;
        this.relatedPropertyDefinition = relatedPropertyDefinition;
        this.entityName = entityName;
    }

    public String getDescription() {
        return description;
    }

    public StepPropertyDefinition getRelatingPropertyDefinition() {
        return relatingPropertyDefinition;
    }

    public StepPropertyDefinition getRelatedPropertyDefinition() {
        return relatedPropertyDefinition;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public StepPropertyDefinition relatingPropertyDefinition() {
        return relatingPropertyDefinition;
    }

    public StepPropertyDefinition relatedPropertyDefinition() {
        return relatedPropertyDefinition;
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
        state.put("relatingPropertyDefinition", relatingPropertyDefinition);
        state.put("relatedPropertyDefinition", relatedPropertyDefinition);
        state.put("entityName", entityName);
        return state;
    }
}
