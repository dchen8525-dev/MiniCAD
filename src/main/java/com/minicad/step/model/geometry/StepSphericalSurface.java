package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal spherical surface semantic record.
 *
 * @param id STEP instance id
 * @param name STEP label
 * @param position sphere placement
 * @param radius sphere radius
 */
/**
 * Minimal spherical surface semantic record.
 *
 * @param id STEP instance id
 * @param name STEP label
 * @param position sphere placement
 * @param radius sphere radius
 */
public final class StepSphericalSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double radius;

    public StepSphericalSurface(int id, String name, StepAxis2Placement3D position, double radius) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSphericalSurface that = (StepSphericalSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius);
    }

    @Override
    public String toString() {
        return "StepSphericalSurface{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "}";
    }
}
