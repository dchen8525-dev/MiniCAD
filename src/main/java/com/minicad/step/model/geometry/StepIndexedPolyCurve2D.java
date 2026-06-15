package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INDEXED_POLY_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param points control points
 * @param indices indices into the point list defining the poly curve
 */
/**
 * Resolved INDEXED_POLY_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param points control points
 * @param indices indices into the point list defining the poly curve
 */
public final class StepIndexedPolyCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepCartesianPoint> points;
    private final List<Integer> indices;

    public StepIndexedPolyCurve2D(int id, String name, List<StepCartesianPoint> points, List<Integer> indices) {
        this.id = id;
        this.name = name;
        this.points = points == null ? null : java.util.List.copyOf(points);
        this.indices = indices == null ? null : java.util.List.copyOf(indices);
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

    public List<Integer> getIndices() {
        return indices;
    }

    // Record-style accessors
    public List<StepCartesianPoint> points() { return getPoints(); }
    public List<Integer> indices() { return getIndices(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIndexedPolyCurve2D that = (StepIndexedPolyCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(points, that.points) && Objects.equals(indices, that.indices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points, indices);
    }

    @Override
    public String toString() {
        return "StepIndexedPolyCurve2D{" + "id=" + id + "name=" + name + "points=" + points + "indices=" + indices + "}";
    }
}
