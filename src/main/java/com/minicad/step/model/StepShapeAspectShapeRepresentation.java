package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SHAPE_ASPECT_SHAPE_REPRESENTATION.
 * Shape representation for shape aspects.
 */
/**
 * Resolved SHAPE_ASPECT_SHAPE_REPRESENTATION.
 * Shape representation for shape aspects.
 */
public final class StepShapeAspectShapeRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity ofShape;

    public StepShapeAspectShapeRepresentation(int id, String name, StepEntity ofShape) {
        this.id = id;
        this.name = name;
        this.ofShape = ofShape;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getOfShape() {
        return ofShape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeAspectShapeRepresentation that = (StepShapeAspectShapeRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(ofShape, that.ofShape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ofShape);
    }

    @Override
    public String toString() {
        return "StepShapeAspectShapeRepresentation{" + "id=" + id + "name=" + name + "ofShape=" + ofShape + "}";
    }
}
