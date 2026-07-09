package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CYLINDER_VOLUME.
 * A CSG cylinder primitive volume.
 */
/**
 * Resolved CYLINDER_VOLUME.
 * A CSG cylinder primitive volume.
 */
public final class StepCylinderVolume implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final Double radius;
    private final Double height;

    public StepCylinderVolume(int id, String name, StepEntity position, Double radius, Double height) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radius = radius;
        this.height = height;
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

    public Double getHeight() {
        return height;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public Double radius() { return getRadius(); }
    public Double height() { return getHeight(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCylinderVolume that = (StepCylinderVolume) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(radius, that.radius) && Objects.equals(height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius, height);
    }

    @Override
    public String toString() {
        return "StepCylinderVolume{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "height=" + height + "}";
    }
}
