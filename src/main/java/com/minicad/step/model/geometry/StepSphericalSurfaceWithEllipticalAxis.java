package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A spherical surface with an elliptical axis definition.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param radius sphere radius
 * @param ellipticalRatio ratio defining the elliptical shape
 */
/**
 * Resolved SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A spherical surface with an elliptical axis definition.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param radius sphere radius
 * @param ellipticalRatio ratio defining the elliptical shape
 */
public final class StepSphericalSurfaceWithEllipticalAxis implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double radius;
    private final double ellipticalRatio;

    public StepSphericalSurfaceWithEllipticalAxis(int id, String name, StepAxis2Placement3D position, double radius, double ellipticalRatio) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radius = radius;
        this.ellipticalRatio = ellipticalRatio;
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

    public double getEllipticalRatio() {
        return ellipticalRatio;
    }

    // Record-style accessors
    public StepAxis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSphericalSurfaceWithEllipticalAxis that = (StepSphericalSurfaceWithEllipticalAxis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && radius == that.radius && ellipticalRatio == that.ellipticalRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius, ellipticalRatio);
    }

    @Override
    public String toString() {
        return "StepSphericalSurfaceWithEllipticalAxis{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "ellipticalRatio=" + ellipticalRatio + "}";
    }
}