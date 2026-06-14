package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal GENERAL_PROPERTY metadata.
 *
 * @param id STEP instance id
 * @param propertyId property identifier
 * @param name property name
 * @param description property description
 */
/**
 * Minimal GENERAL_PROPERTY metadata.
 *
 * @param id STEP instance id
 * @param propertyId property identifier
 * @param name property name
 * @param description property description
 */
public final class StepGeneralProperty implements StepEntity {
    private final int id;
    private final String propertyId;
    private final String name;
    private final String description;

    public StepGeneralProperty(int id, String propertyId, String name, String description) {
        this.id = id;
        this.propertyId = propertyId;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeneralProperty that = (StepGeneralProperty) o;
        return id == that.id && Objects.equals(propertyId, that.propertyId) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, propertyId, name, description);
    }

    @Override
    public String toString() {
        return "StepGeneralProperty{" + "id=" + id + "propertyId=" + propertyId + "name=" + name + "description=" + description + "}";
    }
}
