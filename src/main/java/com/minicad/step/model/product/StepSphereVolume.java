package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SPHERE_VOLUME.
 * A CSG sphere primitive volume.
 */
/**
 * Resolved SPHERE_VOLUME.
 * A CSG sphere primitive volume.
 */
public final class StepSphereVolume implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity center;
    private final Double radius;

    public StepSphereVolume(int id, String name, StepEntity center, Double radius) {
        this.id = id;
        this.name = name;
        this.center = center;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCenter() {
        return center;
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSphereVolume that = (StepSphereVolume) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(center, that.center) && Objects.equals(radius, that.radius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, center, radius);
    }

    @Override
    public String toString() {
        return "StepSphereVolume{" + "id=" + id + "name=" + name + "center=" + center + "radius=" + radius + "}";
    }
}
