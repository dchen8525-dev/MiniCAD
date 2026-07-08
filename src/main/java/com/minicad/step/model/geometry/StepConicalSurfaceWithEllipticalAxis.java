package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A conical surface with an elliptical cross-section.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param semiAngle semi-angle of the cone
 * @param semiAxisA first semi-axis of the ellipse at base
 * @param semiAxisB second semi-axis of the ellipse at base
 */
/**
 * Resolved CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A conical surface with an elliptical cross-section.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param semiAngle semi-angle of the cone
 * @param semiAxisA first semi-axis of the ellipse at base
 * @param semiAxisB second semi-axis of the ellipse at base
 */
public final class StepConicalSurfaceWithEllipticalAxis implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double semiAngle;
    private final double semiAxisA;
    private final double semiAxisB;

    public StepConicalSurfaceWithEllipticalAxis(int id, String name, StepAxis2Placement3D position, double semiAngle, double semiAxisA, double semiAxisB) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.semiAngle = semiAngle;
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

    public double getSemiAngle() {
        return semiAngle;
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
        StepConicalSurfaceWithEllipticalAxis that = (StepConicalSurfaceWithEllipticalAxis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && semiAngle == that.semiAngle && semiAxisA == that.semiAxisA && semiAxisB == that.semiAxisB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, semiAngle, semiAxisA, semiAxisB);
    }

    @Override
    public String toString() {
        return "StepConicalSurfaceWithEllipticalAxis{" + "id=" + id + "name=" + name + "position=" + position + "semiAngle=" + semiAngle + "semiAxisA=" + semiAxisA + "semiAxisB=" + semiAxisB + "}";
    }
}