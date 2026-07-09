package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved AXIS2_PLACEMENT_2D.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param refDirection local x direction
 */
/**
 * Resolved AXIS2_PLACEMENT_2D.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param refDirection local x direction
 */
public final class StepAxis2Placement2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepCartesianPoint location;
    private final StepDirection refDirection;

    public StepAxis2Placement2D(int id, String name, StepCartesianPoint location, StepDirection refDirection) {
        this.id = id;
        this.name = name;
        this.location = location;
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

    public StepDirection getRefDirection() {
        return refDirection;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint location() { return getLocation(); }
    public StepDirection refDirection() { return getRefDirection(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAxis2Placement2D that = (StepAxis2Placement2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(location, that.location) && Objects.equals(refDirection, that.refDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, refDirection);
    }

    @Override
    public String toString() {
        return "StepAxis2Placement2D{" + "id=" + id + "name=" + name + "location=" + location + "refDirection=" + refDirection + "}";
    }
}
