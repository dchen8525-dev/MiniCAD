package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS.
 * A toroidal surface where the axis is defined by a cylindrical axis placement.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 */
/**
 * Resolved TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS.
 * A toroidal surface where the axis is defined by a cylindrical axis placement.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 */
public final class StepToroidalSurfaceWithCylindricalAxis implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis1Placement position;
    private final double majorRadius;
    private final double minorRadius;

    public StepToroidalSurfaceWithCylindricalAxis(int id, String name, StepAxis1Placement position, double majorRadius, double minorRadius) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepAxis1Placement getPosition() {
        return position;
    }

    public double getMajorRadius() {
        return majorRadius;
    }

    public double getMinorRadius() {
        return minorRadius;
    }

    // Record-style accessors
    public StepAxis1Placement position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToroidalSurfaceWithCylindricalAxis that = (StepToroidalSurfaceWithCylindricalAxis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && majorRadius == that.majorRadius && minorRadius == that.minorRadius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, majorRadius, minorRadius);
    }

    @Override
    public String toString() {
        return "StepToroidalSurfaceWithCylindricalAxis{" + "id=" + id + "name=" + name + "position=" + position + "majorRadius=" + majorRadius + "minorRadius=" + minorRadius + "}";
    }
}