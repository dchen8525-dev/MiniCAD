package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved ANGULAR_LOCATION.
 * Location defined by an angular relationship between two shape aspects.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatingShape relating shape aspect
 * @param relatedShape related shape aspect
 */
/**
 * Resolved ANGULAR_LOCATION.
 * Location defined by an angular relationship between two shape aspects.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatingShape relating shape aspect
 * @param relatedShape related shape aspect
 */
public final class StepAngularLocation implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity relatingShape;
    private final StepEntity relatedShape;

    public StepAngularLocation(int id, String name, String description, StepEntity relatingShape, StepEntity relatedShape) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingShape = relatingShape;
        this.relatedShape = relatedShape;
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

    public StepEntity getRelatingShape() {
        return relatingShape;
    }

    public StepEntity getRelatedShape() {
        return relatedShape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAngularLocation that = (StepAngularLocation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingShape, that.relatingShape) && Objects.equals(relatedShape, that.relatedShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingShape, relatedShape);
    }

    @Override
    public String toString() {
        return "StepAngularLocation{" + "id=" + id + "name=" + name + "description=" + description + "relatingShape=" + relatingShape + "relatedShape=" + relatedShape + "}";
    }
}
