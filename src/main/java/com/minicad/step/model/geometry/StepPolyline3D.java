package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved POLYLINE_3D.
 */
/**
 * Resolved POLYLINE_3D.
 */
public final class StepPolyline3D implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> points;

    public StepPolyline3D(int id, String name, List<StepEntity> points) {
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

    public List<StepEntity> getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPolyline3D that = (StepPolyline3D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points);
    }

    @Override
    public String toString() {
        return "StepPolyline3D{" + "id=" + id + "name=" + name + "points=" + points + "}";
    }
}
