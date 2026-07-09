package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved POLYLINE.
 *
 * @param id STEP instance id
 * @param name polyline name
 * @param points polyline vertices
 */
/**
 * Resolved POLYLINE.
 *
 * @param id STEP instance id
 * @param name polyline name
 * @param points polyline vertices
 */
public final class StepPolyline implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepCartesianPoint> points;

    public StepPolyline(int id, String name, List<StepCartesianPoint> points) {
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
        StepPolyline that = (StepPolyline) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points);
    }

    @Override
    public String toString() {
        return "StepPolyline{" + "id=" + id + "name=" + name + "points=" + points + "}";
    }
}
