package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A cylindrical surface with an elliptical cross-section.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param semiAxisA first semi-axis of the ellipse
 * @param semiAxisB second semi-axis of the ellipse
 */
/**
 * Resolved CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A cylindrical surface with an elliptical cross-section.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param semiAxisA first semi-axis of the ellipse
 * @param semiAxisB second semi-axis of the ellipse
 */
public final class StepCylindricalSurfaceWithEllipticalAxis implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double semiAxisA;
    private final double semiAxisB;

    public StepCylindricalSurfaceWithEllipticalAxis(int id, String name, StepAxis2Placement3D position, double semiAxisA, double semiAxisB) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.semiAxisA = semiAxisA;
        this.semiAxisB = semiAxisB;
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

    public double getSemiAxisA() {
        return semiAxisA;
    }

    public double getSemiAxisB() {
        return semiAxisB;
    }

    // Record-style accessors
    public StepAxis2Placement3D position() { return getPosition(); }
    public double semiAxisA() { return getSemiAxisA(); }
    public double semiAxisB() { return getSemiAxisB(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCylindricalSurfaceWithEllipticalAxis that = (StepCylindricalSurfaceWithEllipticalAxis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && semiAxisA == that.semiAxisA && semiAxisB == that.semiAxisB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, semiAxisA, semiAxisB);
    }

    @Override
    public String toString() {
        return "StepCylindricalSurfaceWithEllipticalAxis{" + "id=" + id + "name=" + name + "position=" + position + "semiAxisA=" + semiAxisA + "semiAxisB=" + semiAxisB + "}";
    }
}