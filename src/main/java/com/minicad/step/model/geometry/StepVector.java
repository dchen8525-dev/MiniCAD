package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved VECTOR.
 *
 * @param id step id
 * @param name step label
 * @param orientation referenced direction
 * @param magnitude vector magnitude
 */
/**
 * Resolved VECTOR.
 *
 * @param id step id
 * @param name step label
 * @param orientation referenced direction
 * @param magnitude vector magnitude
 */
public final class StepVector implements StepEntity {
    private final int id;
    private final String name;
    private final StepDirection orientation;
    private final double magnitude;

    public StepVector(int id, String name, StepDirection orientation, double magnitude) {
        this.id = id;
        this.name = name;
        this.orientation = orientation;
        this.magnitude = magnitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepDirection getOrientation() {
        return orientation;
    }

    public double getMagnitude() {
        return magnitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVector that = (StepVector) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(orientation, that.orientation) && magnitude == that.magnitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, orientation, magnitude);
    }

    @Override
    public String toString() {
        return "StepVector{" + "id=" + id + "name=" + name + "orientation=" + orientation + "magnitude=" + magnitude + "}";
    }
}
