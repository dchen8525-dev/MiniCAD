package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal GENERAL_PROPERTY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingGeneralProperty relating property
 * @param relatedGeneralProperty related property
 */
/**
 * Minimal GENERAL_PROPERTY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingGeneralProperty relating property
 * @param relatedGeneralProperty related property
 */
public final class StepGeneralPropertyRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepGeneralProperty relatingGeneralProperty;
    private final StepGeneralProperty relatedGeneralProperty;

    public StepGeneralPropertyRelationship(int id, String name, String description, StepGeneralProperty relatingGeneralProperty, StepGeneralProperty relatedGeneralProperty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingGeneralProperty = relatingGeneralProperty;
        this.relatedGeneralProperty = relatedGeneralProperty;
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

    public StepGeneralProperty getRelatingGeneralProperty() {
        return relatingGeneralProperty;
    }

    public StepGeneralProperty getRelatedGeneralProperty() {
        return relatedGeneralProperty;
    }

    // Record-style accessors
    public StepGeneralProperty relatingGeneralProperty() {
        return relatingGeneralProperty;
    }

    public StepGeneralProperty relatedGeneralProperty() {
        return relatedGeneralProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeneralPropertyRelationship that = (StepGeneralPropertyRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingGeneralProperty, that.relatingGeneralProperty) && Objects.equals(relatedGeneralProperty, that.relatedGeneralProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingGeneralProperty, relatedGeneralProperty);
    }

    @Override
    public String toString() {
        return "StepGeneralPropertyRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingGeneralProperty=" + relatingGeneralProperty + "relatedGeneralProperty=" + relatedGeneralProperty + "}";
    }
}
