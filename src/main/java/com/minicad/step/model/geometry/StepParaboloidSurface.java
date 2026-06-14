package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved PARABOLOID_SURFACE.
 * A quadric surface defined by a paraboloid shape.
 */
/**
 * Resolved PARABOLOID_SURFACE.
 * A quadric surface defined by a paraboloid shape.
 */
public final class StepParaboloidSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final Double focalLength;

    public StepParaboloidSurface(int id, String name, StepEntity position, Double focalLength) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.focalLength = focalLength;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPosition() {
        return position;
    }

    public Double getFocalLength() {
        return focalLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepParaboloidSurface that = (StepParaboloidSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(focalLength, that.focalLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, focalLength);
    }

    @Override
    public String toString() {
        return "StepParaboloidSurface{" + "id=" + id + "name=" + name + "position=" + position + "focalLength=" + focalLength + "}";
    }
}
