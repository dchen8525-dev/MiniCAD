package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BEZIER_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the Bezier curve
 * @param controlPoints control points in 2D
 */
/**
 * Resolved BEZIER_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the Bezier curve
 * @param controlPoints control points in 2D
 */
public final class StepBezierCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;

    public StepBezierCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints) {
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

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public int degree() { return getDegree(); }
    public List<StepCartesianPoint> controlPoints() { return getControlPoints(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBezierCurve2D that = (StepBezierCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && degree == that.degree && Objects.equals(controlPoints, that.controlPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, degree, controlPoints);
    }

    @Override
    public String toString() {
        return "StepBezierCurve2D{" + "id=" + id + "name=" + name + "degree=" + degree + "controlPoints=" + controlPoints + "}";
    }
}
