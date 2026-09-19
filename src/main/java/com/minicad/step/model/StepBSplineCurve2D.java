package com.minicad.step.model;

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
public final class StepBSplineCurve2D extends AbstractStepControlPointCurve {
    public StepBSplineCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm) {
        super(id, name, degree, controlPoints, curveForm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBSplineCurve2D that = (StepBSplineCurve2D) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && getDegree() == that.getDegree() && Objects.equals(getControlPoints(), that.getControlPoints()) && Objects.equals(getCurveForm(), that.getCurveForm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDegree(), getControlPoints(), getCurveForm());
    }

    @Override
    public String toString() {
        return "StepBSplineCurve2D{" + "id=" + getId() + "name=" + getName() + "degree=" + getDegree() + "controlPoints=" + getControlPoints() + "curveForm=" + getCurveForm() + "}";
    }
}
