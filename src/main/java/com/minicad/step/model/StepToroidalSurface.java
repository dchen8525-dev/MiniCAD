package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved TOROIDAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position torus placement
 * @param majorRadius major radius
 * @param minorRadius minor radius
 */
/**
 * Resolved TOROIDAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position torus placement
 * @param majorRadius major radius
 * @param minorRadius minor radius
 */
public final class StepToroidalSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;

    public StepToroidalSurface(int id, String name, StepAxis2Placement3D position, double majorRadius, double minorRadius) {
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

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public double getMajorRadius() {
        return majorRadius;
    }

    public double getMinorRadius() {
        return minorRadius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement3D position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToroidalSurface that = (StepToroidalSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && majorRadius == that.majorRadius && minorRadius == that.minorRadius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, majorRadius, minorRadius);
    }

    @Override
    public String toString() {
        return "StepToroidalSurface{" + "id=" + id + "name=" + name + "position=" + position + "majorRadius=" + majorRadius + "minorRadius=" + minorRadius + "}";
    }
}
