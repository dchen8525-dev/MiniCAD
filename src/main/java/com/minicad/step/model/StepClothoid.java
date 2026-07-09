package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CLOTHOID (Euler spiral / transition curve).
 *
 * @param id STEP instance id
 * @param name clothoid name
 * @param position placement defining the clothoid's local coordinate system
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter of the clothoid
 */
/**
 * Resolved CLOTHOID (Euler spiral / transition curve).
 *
 * @param id STEP instance id
 * @param name clothoid name
 * @param position placement defining the clothoid's local coordinate system
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter of the clothoid
 */
public final class StepClothoid implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final double xAxisIntercept;
    private final double curvature;

    public StepClothoid(int id, String name, StepEntity position, double xAxisIntercept, double curvature) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.xAxisIntercept = xAxisIntercept;
        this.curvature = curvature;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPosition() {
        return position;
    }

    public double getXAxisIntercept() {
        return xAxisIntercept;
    }

    public double getCurvature() {
        return curvature;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public double xAxisIntercept() { return getXAxisIntercept(); }
    public double curvature() { return getCurvature(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepClothoid that = (StepClothoid) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && xAxisIntercept == that.xAxisIntercept && curvature == that.curvature;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, xAxisIntercept, curvature);
    }

    @Override
    public String toString() {
        return "StepClothoid{" + "id=" + id + "name=" + name + "position=" + position + "xAxisIntercept=" + xAxisIntercept + "curvature=" + curvature + "}";
    }
}
