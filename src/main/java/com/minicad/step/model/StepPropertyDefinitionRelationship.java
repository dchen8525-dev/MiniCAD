package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
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
public final class StepPropertyDefinitionRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepPropertyDefinition relatingPropertyDefinition;
    private final StepPropertyDefinition relatedPropertyDefinition;
    private final String entityName;

    public StepPropertyDefinitionRelationship(int id, String name, String description, StepPropertyDefinition relatingPropertyDefinition, StepPropertyDefinition relatedPropertyDefinition, String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingPropertyDefinition = relatingPropertyDefinition;
        this.relatedPropertyDefinition = relatedPropertyDefinition;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPropertyDefinitionRelationship that = (StepPropertyDefinitionRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingPropertyDefinition, that.relatingPropertyDefinition) && Objects.equals(relatedPropertyDefinition, that.relatedPropertyDefinition) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingPropertyDefinition, relatedPropertyDefinition, entityName);
    }

    @Override
    public String toString() {
        return "StepPropertyDefinitionRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingPropertyDefinition=" + relatingPropertyDefinition + "relatedPropertyDefinition=" + relatedPropertyDefinition + "entityName=" + entityName + "}";
    }
}
