package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.geometry.StepAxis2Placement2D;
import java.util.Objects;
/**
 * Resolved CENTRE_LINE_ARC_PROFILE_DEF.
 * An arc profile defined along its centre line.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param radius arc radius
 * @param angle sweep angle
 */
/**
 * Resolved CENTRE_LINE_ARC_PROFILE_DEF.
 * An arc profile defined along its centre line.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param radius arc radius
 * @param angle sweep angle
 */
public final class StepCentreLineArcProfileDef implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement2D position;
    private final double radius;
    private final double angle;

    public StepCentreLineArcProfileDef(int id, String name, StepAxis2Placement2D position, double radius, double angle) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radius = radius;
        this.angle = angle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getAngle() {
        return angle;
    }

    // Record-style accessors
    public double radius() { return radius; }
    public double angle() { return angle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCentreLineArcProfileDef that = (StepCentreLineArcProfileDef) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && radius == that.radius && angle == that.angle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius, angle);
    }

    @Override
    public String toString() {
        return "StepCentreLineArcProfileDef{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "angle=" + angle + "}";
    }
}
