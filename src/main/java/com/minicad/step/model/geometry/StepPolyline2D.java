package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved POLYLINE_2D.
 *
 * @param id step id
 * @param name step label
 * @param points ordered list of points defining the polyline
 */
/**
 * Resolved POLYLINE_2D.
 *
 * @param id step id
 * @param name step label
 * @param points ordered list of points defining the polyline
 */
public final class StepPolyline2D implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepCartesianPoint> points;

    public StepPolyline2D(int id, String name, List<StepCartesianPoint> points) {
        this.id = id;
        this.name = name;
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepCartesianPoint> getPoints() {
        return points;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepCartesianPoint> points() { return getPoints(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPolyline2D that = (StepPolyline2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points);
    }

    @Override
    public String toString() {
        return "StepPolyline2D{" + "id=" + id + "name=" + name + "points=" + points + "}";
    }
}
