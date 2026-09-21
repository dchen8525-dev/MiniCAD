package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relating source product definition relationship
 * @param related target product definition relationship
 * @param entityName concrete STEP entity name
 */
public final class StepProductDefinitionRelationshipRelationship extends AbstractStepEntity {
    private final String identifier;
    private final String description;
    private final StepProductDefinitionRelationship relating;
    private final StepProductDefinitionRelationship related;
    private final String entityName;

    public StepProductDefinitionRelationshipRelationship(int id, String identifier, String name, String description, StepProductDefinitionRelationship relating, StepProductDefinitionRelationship related, String entityName) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
        this.relating = relating;
        this.related = related;
        this.entityName = entityName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public StepProductDefinitionRelationship getRelating() {
        return relating;
    }

    public StepProductDefinitionRelationship getRelated() {
        return related;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    // Record-style accessors
    public StepProductDefinitionRelationship relating() {
        return relating;
    }

    public StepProductDefinitionRelationship related() {
        return related;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("identifier", identifier);
        state.put("name", getName());
        state.put("description", description);
        state.put("relating", relating);
        state.put("related", related);
        state.put("entityName", entityName);
        return state;
    }
}
