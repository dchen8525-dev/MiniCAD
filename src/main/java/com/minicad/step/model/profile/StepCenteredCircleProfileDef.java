package com.minicad.step.model.profile;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepAxis2Placement2D;
import java.util.Objects;
/**
 * Resolved CENTERED_CIRCLE_PROFILE_DEF.
 * A circular profile with explicit center offset.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param radius circle radius
 * @param centerOffset center offset distance
 */
/**
 * Resolved CENTERED_CIRCLE_PROFILE_DEF.
 * A circular profile with explicit center offset.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param radius circle radius
 * @param centerOffset center offset distance
 */
public final class StepCenteredCircleProfileDef implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement2D position;
    private final double radius;
    private final double centerOffset;

    public StepCenteredCircleProfileDef(int id, String name, StepAxis2Placement2D position, double radius, double centerOffset) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.radius = radius;
        this.centerOffset = centerOffset;
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

    public double getCenterOffset() {
        return centerOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCenteredCircleProfileDef that = (StepCenteredCircleProfileDef) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && radius == that.radius && centerOffset == that.centerOffset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, radius, centerOffset);
    }

    @Override
    public String toString() {
        return "StepCenteredCircleProfileDef{" + "id=" + id + "name=" + name + "position=" + position + "radius=" + radius + "centerOffset=" + centerOffset + "}";
    }
}
