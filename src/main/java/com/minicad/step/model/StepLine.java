package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved LINE.
 *
 * @param id step id
 * @param name step label
 * @param point line origin
 * @param vector line direction vector
 */
/**
 * Resolved LINE.
 *
 * @param id step id
 * @param name step label
 * @param point line origin
 * @param vector line direction vector
 */
public final class StepLine implements StepEntity {
    private final int id;
    private final String name;
    private final StepCartesianPoint point;
    private final StepVector vector;

    public StepLine(int id, String name, StepCartesianPoint point, StepVector vector) {
        this.id = id;
        this.name = name;
        this.point = point;
        this.vector = vector;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepCartesianPoint getPoint() {
        return point;
    }

    public StepVector getVector() {
        return vector;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint point() { return getPoint(); }
    public StepVector vector() { return getVector(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLine that = (StepLine) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(point, that.point) && Objects.equals(vector, that.vector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, point, vector);
    }

    @Override
    public String toString() {
        return "StepLine{" + "id=" + id + "name=" + name + "point=" + point + "vector=" + vector + "}";
    }
}
