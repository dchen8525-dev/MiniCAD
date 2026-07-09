package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SHAPE_REPRESENTATION_TRANSFORMATION.
 * A transformation between shape representations.
 */
/**
 * Resolved SHAPE_REPRESENTATION_TRANSFORMATION.
 * A transformation between shape representations.
 */
public final class StepShapeRepresentationTransformation implements StepEntity {
    private final int id;
    private final String name;
    private final String transformationType;
    private final StepEntity transformation;

    public StepShapeRepresentationTransformation(int id, String name, String transformationType, StepEntity transformation) {
        this.id = id;
        this.name = name;
        this.transformationType = transformationType;
        this.transformation = transformation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTransformationType() {
        return transformationType;
    }

    public StepEntity getTransformation() {
        return transformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeRepresentationTransformation that = (StepShapeRepresentationTransformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transformationType, that.transformationType) && Objects.equals(transformation, that.transformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transformationType, transformation);
    }

    @Override
    public String toString() {
        return "StepShapeRepresentationTransformation{" + "id=" + id + "name=" + name + "transformationType=" + transformationType + "transformation=" + transformation + "}";
    }
}
