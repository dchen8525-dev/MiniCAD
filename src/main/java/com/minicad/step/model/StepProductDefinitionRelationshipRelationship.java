package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
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
public final class StepProductDefinitionRelationshipRelationship implements StepEntity {
    private final int id;
    private final String identifier;
    private final String name;
    private final String description;
    private final StepProductDefinitionRelationship relating;
    private final StepProductDefinitionRelationship related;
    private final String entityName;

    public StepProductDefinitionRelationshipRelationship(int id, String identifier, String name, String description, StepProductDefinitionRelationship relating, StepProductDefinitionRelationship related, String entityName) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.relating = relating;
        this.related = related;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinitionRelationshipRelationship that = (StepProductDefinitionRelationshipRelationship) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relating, that.relating) && Objects.equals(related, that.related) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name, description, relating, related, entityName);
    }

    @Override
    public String toString() {
        return "StepProductDefinitionRelationshipRelationship{" + "id=" + id + "identifier=" + identifier + "name=" + name + "description=" + description + "relating=" + relating + "related=" + related + "entityName=" + entityName + "}";
    }
}
