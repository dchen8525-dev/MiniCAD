package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved AXIS1_PLACEMENT.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param axis axis direction
 */
/**
 * Resolved AXIS1_PLACEMENT.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param axis axis direction
 */
public final class StepAxis1Placement implements StepEntity {
    private final int id;
    private final String name;
    private final StepCartesianPoint location;
    private final StepDirection axis;

    public StepAxis1Placement(int id, String name, StepCartesianPoint location, StepDirection axis) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.axis = axis;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAxis1Placement that = (StepAxis1Placement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(location, that.location) && Objects.equals(axis, that.axis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, axis);
    }

    @Override
    public String toString() {
        return "StepAxis1Placement{" + "id=" + id + "name=" + name + "location=" + location + "axis=" + axis + "}";
    }
}
