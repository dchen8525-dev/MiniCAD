package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
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
public final class StepProductDefinitionRelationship implements StepEntity {
    private final int id;
    private final String identifier;
    private final String name;
    private final String description;
    private final StepProductDefinition relatingProductDefinition;
    private final StepProductDefinition relatedProductDefinition;
    private final String entityName;

    public StepProductDefinitionRelationship(int id, String identifier, String name, String description, StepProductDefinition relatingProductDefinition, StepProductDefinition relatedProductDefinition, String entityName) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.relatingProductDefinition = relatingProductDefinition;
        this.relatedProductDefinition = relatedProductDefinition;
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

    public StepProductDefinition getRelatingProductDefinition() {
        return relatingProductDefinition;
    }

    public StepProductDefinition getRelatedProductDefinition() {
        return relatedProductDefinition;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinitionRelationship that = (StepProductDefinitionRelationship) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingProductDefinition, that.relatingProductDefinition) && Objects.equals(relatedProductDefinition, that.relatedProductDefinition) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name, description, relatingProductDefinition, relatedProductDefinition, entityName);
    }

    @Override
    public String toString() {
        return "StepProductDefinitionRelationship{" + "id=" + id + "identifier=" + identifier + "name=" + name + "description=" + description + "relatingProductDefinition=" + relatingProductDefinition + "relatedProductDefinition=" + relatedProductDefinition + "entityName=" + entityName + "}";
    }
}
