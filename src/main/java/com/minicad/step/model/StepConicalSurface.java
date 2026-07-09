package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved CONICAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position surface placement
 * @param radius radius at placement origin
 * @param semiAngle semi-angle in radians
 */
/**
 * Resolved CONICAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position surface placement
 * @param radius radius at placement origin
 * @param semiAngle semi-angle in radians
 */
public final class StepConicalSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double radius;
    private final double semiAngle;

    public StepConicalSurface(int id, String name, StepAxis2Placement3D position, double radius, double semiAngle) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radius = radius;
        this.semiAngle = semiAngle;
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

    public double getSemiAngle() {
        return semiAngle;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }
    public double semiAngle() { return getSemiAngle(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConicalSurface that = (StepConicalSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && radius == that.radius && semiAngle == that.semiAngle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius, semiAngle);
    }

    @Override
    public String toString() {
        return "StepConicalSurface{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "semiAngle=" + semiAngle + "}";
    }
}
