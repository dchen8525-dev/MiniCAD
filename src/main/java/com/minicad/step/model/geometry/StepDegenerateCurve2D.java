package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DEGENERATE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param point the degenerate point
 */
/**
 * Resolved DEGENERATE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param point the degenerate point
 */
public final class StepDegenerateCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepCartesianPoint point;

    public StepDegenerateCurve2D(int id, String name, StepCartesianPoint point) {
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

    // Record-style accessors
    public StepCartesianPoint point() { return getPoint(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDegenerateCurve2D that = (StepDegenerateCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, point);
    }

    @Override
    public String toString() {
        return "StepDegenerateCurve2D{" + "id=" + id + "name=" + name + "point=" + point + "}";
    }
}
