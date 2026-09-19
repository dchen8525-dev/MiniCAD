package com.minicad.step.model;

import java.util.List;
import java.util.Objects;

/**
 * Resolved UNIFORM_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the curve
 * @param controlPoints control points in 2D
 * @param curveForm the form of the curve
 */
public final class StepUniformCurve2D extends AbstractStepControlPointCurve {
    public StepUniformCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm) {
        super(id, name, degree, controlPoints, curveForm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepUniformCurve2D that = (StepUniformCurve2D) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && getDegree() == that.getDegree() && Objects.equals(getControlPoints(), that.getControlPoints()) && Objects.equals(getCurveForm(), that.getCurveForm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDegree(), getControlPoints(), getCurveForm());
    }

    @Override
    public String toString() {
        return "StepUniformCurve2D{" + "id=" + getId() + "name=" + getName() + "degree=" + getDegree() + "controlPoints=" + getControlPoints() + "curveForm=" + getCurveForm() + "}";
    }
}
