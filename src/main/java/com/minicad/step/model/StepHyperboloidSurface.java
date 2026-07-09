package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved HYPERBOLOID_SURFACE.
 * A quadric surface defined by a hyperboloid shape (one-sheet or two-sheet).
 */
/**
 * Resolved HYPERBOLOID_SURFACE.
 * A quadric surface defined by a hyperboloid shape (one-sheet or two-sheet).
 */
public final class StepHyperboloidSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final Double radius;
    private final Double semiAxis;

    public StepHyperboloidSurface(int id, String name, StepEntity position, Double radius, Double semiAxis) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radius = radius;
        this.semiAxis = semiAxis;
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

    public Double getRadius() {
        return radius;
    }

    public Double getSemiAxis() {
        return semiAxis;
    }

    // Record-style accessors
    public StepEntity position() { return getPosition(); }
    public Double radius() { return getRadius(); }
    public Double semiAxis() { return getSemiAxis(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHyperboloidSurface that = (StepHyperboloidSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(radius, that.radius) && Objects.equals(semiAxis, that.semiAxis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius, semiAxis);
    }

    @Override
    public String toString() {
        return "StepHyperboloidSurface{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "semiAxis=" + semiAxis + "}";
    }
}
