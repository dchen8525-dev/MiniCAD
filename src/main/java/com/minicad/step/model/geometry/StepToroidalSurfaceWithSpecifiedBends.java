package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS.
 * A toroidal surface where the major and minor axes are optionally defined by explicit curves.
 * For B-Rep generation, this is treated as a standard toroidal surface using the radius parameters.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 * @param majorAxisCurve optional curve defining the major axis path
 * @param minorAxisCurve optional curve defining the minor axis profile
 */
/**
 * Resolved TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS.
 * A toroidal surface where the major and minor axes are optionally defined by explicit curves.
 * For B-Rep generation, this is treated as a standard toroidal surface using the radius parameters.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 * @param majorAxisCurve optional curve defining the major axis path
 * @param minorAxisCurve optional curve defining the minor axis profile
 */
public final class StepToroidalSurfaceWithSpecifiedBends implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;
    private final StepEntity majorAxisCurve;
    private final StepEntity minorAxisCurve;

    public StepToroidalSurfaceWithSpecifiedBends(int id, String name, StepAxis2Placement3D position, double majorRadius, double minorRadius, StepEntity majorAxisCurve, StepEntity minorAxisCurve) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.majorAxisCurve = majorAxisCurve;
        this.minorAxisCurve = minorAxisCurve;
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

    public StepEntity getMajorAxisCurve() {
        return majorAxisCurve;
    }

    public StepEntity getMinorAxisCurve() {
        return minorAxisCurve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToroidalSurfaceWithSpecifiedBends that = (StepToroidalSurfaceWithSpecifiedBends) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && majorRadius == that.majorRadius && minorRadius == that.minorRadius && Objects.equals(majorAxisCurve, that.majorAxisCurve) && Objects.equals(minorAxisCurve, that.minorAxisCurve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, majorRadius, minorRadius, majorAxisCurve, minorAxisCurve);
    }

    @Override
    public String toString() {
        return "StepToroidalSurfaceWithSpecifiedBends{" + "id=" + id + "name=" + name + "position=" + position + "majorRadius=" + majorRadius + "minorRadius=" + minorRadius + "majorAxisCurve=" + majorAxisCurve + "minorAxisCurve=" + minorAxisCurve + "}";
    }
}
