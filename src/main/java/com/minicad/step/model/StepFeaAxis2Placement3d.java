package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FEA_AXIS_2_PLACEMENT_3D.
 * A 3D axis placement for finite element coordinate systems.
 */
/**
 * Resolved FEA_AXIS_2_PLACEMENT_3D.
 * A 3D axis placement for finite element coordinate systems.
 */
public final class StepFeaAxis2Placement3d implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity location;
    private final StepEntity axis;
    private final StepEntity refDirection;

    public StepFeaAxis2Placement3d(int id, String name, StepEntity location, StepEntity axis, StepEntity refDirection) {
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

    public StepEntity getLocation() {
        return location;
    }

    public StepEntity getAxis() {
        return axis;
    }

    public StepEntity getRefDirection() {
        return refDirection;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity location() { return getLocation(); }
    public StepEntity axis() { return getAxis(); }
    public StepEntity refDirection() { return getRefDirection(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaAxis2Placement3d that = (StepFeaAxis2Placement3d) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(location, that.location) && Objects.equals(axis, that.axis) && Objects.equals(refDirection, that.refDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, axis, refDirection);
    }

    @Override
    public String toString() {
        return "StepFeaAxis2Placement3d{" + "id=" + id + "name=" + name + "location=" + location + "axis=" + axis + "refDirection=" + refDirection + "}";
    }
}
