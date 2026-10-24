package com.minicad.step.model;

import java.util.Map;

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
public final class StepRepresentationItemRelationship extends AbstractStepEntity {
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
        super(id, name);
        this.description = description;
        this.relatingRepresentationItem = relatingRepresentationItem;
        this.relatedRepresentationItem = relatedRepresentationItem;
        this.entityName = entityName;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description,
                "relatingRepresentationItem", relatingRepresentationItem,
                "relatedRepresentationItem", relatedRepresentationItem, "entityName", entityName);
    }
}
