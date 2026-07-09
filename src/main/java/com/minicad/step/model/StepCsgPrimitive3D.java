package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved CSG_PRIMITIVE_3D.
 */
/**
 * Resolved CSG_PRIMITIVE_3D.
 */
public final class StepCsgPrimitive3D implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;

    public StepCsgPrimitive3D(int id, String name, StepEntity position) {
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

    public StepEntity getPosition() {
        return position;
    }

    // Record-style accessor
    public StepEntity position() { return getPosition(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCsgPrimitive3D that = (StepCsgPrimitive3D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position);
    }

    @Override
    public String toString() {
        return "StepCsgPrimitive3D{" + "id=" + id + "name=" + name + "position=" + position + "}";
    }
}
