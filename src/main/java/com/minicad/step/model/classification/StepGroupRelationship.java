package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
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
public final class StepGroupRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepGroup relatingGroup;
    private final StepGroup relatedGroup;
    private final String entityName;

    public StepGroupRelationship(int id, String name, String description, StepGroup relatingGroup, StepGroup relatedGroup, String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingGroup = relatingGroup;
        this.relatedGroup = relatedGroup;
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

    public StepGroup getRelatingGroup() {
        return relatingGroup;
    }

    public StepGroup getRelatedGroup() {
        return relatedGroup;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGroupRelationship that = (StepGroupRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingGroup, that.relatingGroup) && Objects.equals(relatedGroup, that.relatedGroup) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingGroup, relatedGroup, entityName);
    }

    @Override
    public String toString() {
        return "StepGroupRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingGroup=" + relatingGroup + "relatedGroup=" + relatedGroup + "entityName=" + entityName + "}";
    }
}
