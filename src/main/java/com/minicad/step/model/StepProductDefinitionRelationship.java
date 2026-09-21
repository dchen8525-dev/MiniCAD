package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PRODUCT_DEFINITION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relatingProductDefinition source product definition
 * @param relatedProductDefinition target product definition
 * @param entityName concrete STEP entity name
 */
public final class StepProductDefinitionRelationship extends AbstractStepEntity {
    private final String identifier;
    private final String description;
    private final StepProductDefinition relatingProductDefinition;
    private final StepProductDefinition relatedProductDefinition;
    private final String entityName;

    public StepProductDefinitionRelationship(int id, String identifier, String name, String description, StepProductDefinition relatingProductDefinition, StepProductDefinition relatedProductDefinition, String entityName) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
        this.relatingProductDefinition = relatingProductDefinition;
        this.relatedProductDefinition = relatedProductDefinition;
        this.entityName = entityName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public StepProductDefinition getRelatingProductDefinition() {
        return relatingProductDefinition;
    }

    public StepProductDefinition getRelatedProductDefinition() {
        return relatedProductDefinition;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public String identifier() {
        return identifier;
    }

    public String name() {
        return getName();
    }

    public String description() {
        return description;
    }

    public StepProductDefinition relatingProductDefinition() {
        return relatingProductDefinition;
    }

    public StepProductDefinition relatedProductDefinition() {
        return relatedProductDefinition;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("identifier", identifier);
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingProductDefinition", relatingProductDefinition);
        state.put("relatedProductDefinition", relatedProductDefinition);
        state.put("entityName", entityName);
        return state;
    }
}
