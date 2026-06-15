package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved PLANE.
 *
 * @param id step id
 * @param name step label
 * @param position plane placement
 */
/**
 * Resolved PLANE.
 *
 * @param id step id
 * @param name step label
 * @param position plane placement
 */
public final class StepPlane implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;

    public StepPlane(int id, String name, StepAxis2Placement3D position) {
        this.id = id;
        this.name = name;
        this.position = position;
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

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement3D position() { return getPosition(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlane that = (StepPlane) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position);
    }

    @Override
    public String toString() {
        return "StepPlane{" + "id=" + id + "name=" + name + "position=" + position + "}";
    }
}
