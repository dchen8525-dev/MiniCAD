package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved AXIS2_PLACEMENT_3D with explicit axis and ref direction.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param axis local Z direction
 * @param refDirection local X direction
 */
/**
 * Resolved AXIS2_PLACEMENT_3D with explicit axis and ref direction.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param axis local Z direction
 * @param refDirection local X direction
 */
public final class StepAxis2Placement3D implements StepEntity {
    private final int id;
    private final String name;
    private final StepCartesianPoint location;
    private final StepDirection axis;
    private final StepDirection refDirection;

    public StepAxis2Placement3D(int id, String name, StepCartesianPoint location, StepDirection axis, StepDirection refDirection) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.axis = axis;
        this.refDirection = refDirection;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepCartesianPoint getLocation() {
        return location;
    }

    public StepDirection getAxis() {
        return axis;
    }

    public StepDirection getRefDirection() {
        return refDirection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAxis2Placement3D that = (StepAxis2Placement3D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(location, that.location) && Objects.equals(axis, that.axis) && Objects.equals(refDirection, that.refDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, axis, refDirection);
    }

    @Override
    public String toString() {
        return "StepAxis2Placement3D{" + "id=" + id + "name=" + name + "location=" + location + "axis=" + axis + "refDirection=" + refDirection + "}";
    }
}
