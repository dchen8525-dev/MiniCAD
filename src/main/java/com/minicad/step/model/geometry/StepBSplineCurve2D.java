package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved B_SPLINE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the B-spline
 * @param controlPoints control points in 2D
 * @param curveForm the form of the curve
 */
/**
 * Resolved B_SPLINE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the B-spline
 * @param controlPoints control points in 2D
 * @param curveForm the form of the curve
 */
public final class StepBSplineCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;
    private final String curveForm;

    public StepBSplineCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.curveForm = curveForm;
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

    public String getCurveForm() {
        return curveForm;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public int degree() { return getDegree(); }
    public List<StepCartesianPoint> controlPoints() { return getControlPoints(); }
    public String curveForm() { return getCurveForm(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBSplineCurve2D that = (StepBSplineCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(curveForm, that.curveForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, degree, controlPoints, curveForm);
    }

    @Override
    public String toString() {
        return "StepBSplineCurve2D{" + "id=" + id + "name=" + name + "degree=" + degree + "controlPoints=" + controlPoints + "curveForm=" + curveForm + "}";
    }
}
