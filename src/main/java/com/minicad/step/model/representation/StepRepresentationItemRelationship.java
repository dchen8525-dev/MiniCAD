package com.minicad.step.model.representation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * REPRESENTATION_ITEM_RELATIONSHIP entity model.
 * Represents a relationship between two representation items.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param description optional description text
 * @param relatingRepresentationItem reference to relating representation item
 * @param relatedRepresentationItem reference to related representation item
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepRepresentationItemRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description; // OPTIONAL
    private final Object relatingRepresentationItem; // representation_item reference
    private final Object relatedRepresentationItem; // representation_item reference
    private final String entityName;

    public StepRepresentationItemRelationship(
        int id,
        String name,
        String description,
        Object relatingRepresentationItem,
        Object relatedRepresentationItem,
        String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingRepresentationItem = relatingRepresentationItem;
        this.relatedRepresentationItem = relatedRepresentationItem;
        this.entityName = entityName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Object getRelatingRepresentationItem() {
        return relatingRepresentationItem;
    }

    public Object getRelatedRepresentationItem() {
        return relatedRepresentationItem;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepresentationItemRelationship that = (StepRepresentationItemRelationship) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepRepresentationItemRelationship{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", relatingRepresentationItem=" + relatingRepresentationItem +
            ", relatedRepresentationItem=" + relatedRepresentationItem +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}