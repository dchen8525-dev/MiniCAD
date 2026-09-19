package com.minicad.step.model;

import java.util.List;
import java.util.Objects;

/**
 * Resolved RATIONAL_B_SPLINE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the B-spline
 * @param controlPoints control points in 2D
 * @param weights weights for each control point
 * @param curveForm the form of the curve
 */
public final class StepRationalBSplineCurve2D extends AbstractStepControlPointCurve {
    private final List<Double> weights;

    public StepRationalBSplineCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints, List<Double> weights, String curveForm) {
        super(id, name, degree, controlPoints, curveForm);
        this.weights = weights == null ? null : java.util.List.copyOf(weights);
    }

    public List<Double> getWeights() {
        return weights;
    }

    // Record-style accessors
    public List<Double> weights() { return getWeights(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRationalBSplineCurve2D that = (StepRationalBSplineCurve2D) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && getDegree() == that.getDegree() && Objects.equals(getControlPoints(), that.getControlPoints()) && Objects.equals(weights, that.weights) && Objects.equals(getCurveForm(), that.getCurveForm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDegree(), getControlPoints(), weights, getCurveForm());
    }

    @Override
    public String toString() {
        return "StepRationalBSplineCurve2D{" + "id=" + getId() + "name=" + getName() + "degree=" + getDegree() + "controlPoints=" + getControlPoints() + "weights=" + weights + "curveForm=" + getCurveForm() + "}";
    }
}
