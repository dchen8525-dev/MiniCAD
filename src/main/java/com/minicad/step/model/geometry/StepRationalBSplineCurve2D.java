package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
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
public final class StepRationalBSplineCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;
    private final List<Double> weights;
    private final String curveForm;

    public StepRationalBSplineCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints, List<Double> weights, String curveForm) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.weights = weights == null ? null : java.util.List.copyOf(weights);
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

    public List<Double> getWeights() {
        return weights;
    }

    public String getCurveForm() {
        return curveForm;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public int degree() { return getDegree(); }
    public List<StepCartesianPoint> controlPoints() { return getControlPoints(); }
    public List<Double> weights() { return getWeights(); }
    public String curveForm() { return getCurveForm(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRationalBSplineCurve2D that = (StepRationalBSplineCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(weights, that.weights) && Objects.equals(curveForm, that.curveForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, degree, controlPoints, weights, curveForm);
    }

    @Override
    public String toString() {
        return "StepRationalBSplineCurve2D{" + "id=" + id + "name=" + name + "degree=" + degree + "controlPoints=" + controlPoints + "weights=" + weights + "curveForm=" + curveForm + "}";
    }
}
