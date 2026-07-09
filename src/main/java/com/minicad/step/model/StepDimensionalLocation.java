package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DIMENSIONAL_LOCATION.
 * A dimensional location between two shape aspects.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatedShape referenced shape aspect
 */
/**
 * Resolved DIMENSIONAL_LOCATION.
 * A dimensional location between two shape aspects.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatedShape referenced shape aspect
 */
public final class StepDimensionalLocation implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity relatedShape;

    public StepDimensionalLocation(int id, String name, String description, StepEntity relatedShape) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public StepEntity getRelatedShape() {
        return relatedShape;
    }

    // Record-style accessor
    public StepEntity relatedShape() {
        return relatedShape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDimensionalLocation that = (StepDimensionalLocation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatedShape, that.relatedShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatedShape);
    }

    @Override
    public String toString() {
        return "StepDimensionalLocation{" + "id=" + id + "name=" + name + "description=" + description + "relatedShape=" + relatedShape + "}";
    }
}
