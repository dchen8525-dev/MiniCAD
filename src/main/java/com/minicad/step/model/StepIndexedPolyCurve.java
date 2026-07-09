package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INDEXED_POLY_CURVE / INDEXED_POLYCURVE (MiniCAD alias).
 */
/**
 * Resolved INDEXED_POLY_CURVE / INDEXED_POLYCURVE (MiniCAD alias).
 */
public final class StepIndexedPolyCurve implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepCartesianPoint> points;
    private final List<Integer> indices;
    private final boolean closed;

    public StepIndexedPolyCurve(int id, String name, List<StepCartesianPoint> points, List<Integer> indices, boolean closed) {
        this.id = id;
        this.name = name;
        this.points = points == null ? null : java.util.List.copyOf(points);
        this.indices = indices == null ? null : java.util.List.copyOf(indices);
        this.closed = closed;
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

    public boolean isClosed() {
        return closed;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepCartesianPoint> points() { return getPoints(); }
    public List<Integer> indices() { return getIndices(); }
    public boolean closed() { return isClosed(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIndexedPolyCurve that = (StepIndexedPolyCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(points, that.points) && Objects.equals(indices, that.indices) && closed == that.closed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, points, indices, closed);
    }

    @Override
    public String toString() {
        return "StepIndexedPolyCurve{" + "id=" + id + "name=" + name + "points=" + points + "indices=" + indices + "closed=" + closed + "}";
    }
}
