package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepCartesianPoint;
import java.util.Objects;
/**
 * Resolved VERTEX_POINT.
 *
 * @param id step id
 * @param name step label
 * @param point referenced point geometry
 */
/**
 * Resolved VERTEX_POINT.
 *
 * @param id step id
 * @param name step label
 * @param point referenced point geometry
 */
public final class StepVertexPoint implements StepEntity {
    private final int id;
    private final String name;
    private final StepCartesianPoint point;

    public StepVertexPoint(int id, String name, StepCartesianPoint point) {
        this.id = id;
        this.name = name;
        this.point = point;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVertexPoint that = (StepVertexPoint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, point);
    }

    @Override
    public String toString() {
        return "StepVertexPoint{" + "id=" + id + "name=" + name + "point=" + point + "}";
    }
}
