package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A toroidal surface where the axis is defined by an elliptical axis placement.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 * @param ellipticalRatio ratio defining the elliptical cross-section
 */
/**
 * Resolved TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A toroidal surface where the axis is defined by an elliptical axis placement.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 * @param ellipticalRatio ratio defining the elliptical cross-section
 */
public final class StepToroidalSurfaceWithEllipticalAxis implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;
    private final double ellipticalRatio;

    public StepToroidalSurfaceWithEllipticalAxis(int id, String name, StepAxis2Placement3D position, double majorRadius, double minorRadius, double ellipticalRatio) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
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

    public double getMajorRadius() {
        return majorRadius;
    }

    public double getMinorRadius() {
        return minorRadius;
    }

    public double getEllipticalRatio() {
        return ellipticalRatio;
    }

    // Record-style accessors
    public StepAxis2Placement3D position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToroidalSurfaceWithEllipticalAxis that = (StepToroidalSurfaceWithEllipticalAxis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && majorRadius == that.majorRadius && minorRadius == that.minorRadius && ellipticalRatio == that.ellipticalRatio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, majorRadius, minorRadius, ellipticalRatio);
    }

    @Override
    public String toString() {
        return "StepToroidalSurfaceWithEllipticalAxis{" + "id=" + id + "name=" + name + "position=" + position + "majorRadius=" + majorRadius + "minorRadius=" + minorRadius + "ellipticalRatio=" + ellipticalRatio + "}";
    }
}