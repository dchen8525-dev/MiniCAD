package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PIECEWISE_BEZIER_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the curve
 * @param controlPoints control points in 2D
 */
/**
 * Resolved PIECEWISE_BEZIER_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the curve
 * @param controlPoints control points in 2D
 */
public final class StepPiecewiseBezierCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;

    public StepPiecewiseBezierCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDegree() {
        return degree;
    }

    public List<StepCartesianPoint> getControlPoints() {
        return controlPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPiecewiseBezierCurve2D that = (StepPiecewiseBezierCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && degree == that.degree && Objects.equals(controlPoints, that.controlPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, degree, controlPoints);
    }

    @Override
    public String toString() {
        return "StepPiecewiseBezierCurve2D{" + "id=" + id + "name=" + name + "degree=" + degree + "controlPoints=" + controlPoints + "}";
    }
}
