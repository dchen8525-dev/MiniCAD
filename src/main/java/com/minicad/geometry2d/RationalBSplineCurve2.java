package com.minicad.geometry2d;

import com.minicad.common.BSplineKernel;
import com.minicad.common.GeometryException;
import com.minicad.common.KnotVector;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal rational 2D B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param weights weights for control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class RationalBSplineCurve2 implements Curve2 {
    private final int degree;
    private final List<Point2> controlPoints;
    private final List<Double> weights;
    private final KnotVector knotVector;

    public RationalBSplineCurve2(int degree, List<Point2> controlPoints, List<Double> weights, List<Integer> knotMultiplicities, List<Double> knots) {
        BSplineCurve2.validateDefinition(degree, controlPoints, knotMultiplicities, knots);
        if (weights == null || weights.size() != controlPoints.size()) {
            throw new GeometryException("weight count must match control point count");
        }
        for (double weight : weights) {
            if (!Double.isFinite(weight) || weight <= 0.0) {
                throw new GeometryException("weights must be finite and positive");
            }
        }
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.weights = weights == null ? null : java.util.List.copyOf(weights);
        this.knotVector = new KnotVector(knots, knotMultiplicities);
    }

    public int getDegree() {
        return degree;
    }

    public List<Point2> getControlPoints() {
        return controlPoints;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public List<Integer> getKnotMultiplicities() {
        return knotVector.multiplicities();
    }

    public List<Double> getKnots() {
        return knotVector.knots();
    }

    // Record-style accessors
    public int degree() { return getDegree(); }
    public List<Point2> controlPoints() { return getControlPoints(); }
    public List<Double> weights() { return getWeights(); }
    public List<Integer> knotMultiplicities() { return getKnotMultiplicities(); }
    public List<Double> knots() { return getKnots(); }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (controlPoints == null || controlPoints.isEmpty() || weights == null) {
            return new Point2(0, 0);
        }
        List<Double> expanded = expandedKnots();
        if (expanded.size() <= degree + 1) {
            return new Point2(0, 0);
        }
        return BSplineMath2.evaluateRational(controlPoints, weights, degree, parameter, expanded);
    }

    /**
     * Returns the expanded knot vector (with multiplicities expanded).
     * Cached after first use to avoid repeated allocation on evaluation hot paths.
     */
    public List<Double> expandedKnots() {
        return knotVector.expanded();
    }

    /**
     * Returns the start parameter of the curve (first knot value).
     *
     * @return start parameter
     */
    public double startParameter() {
        return knotVector.start();
    }

    /**
     * Returns the end parameter of the curve (last knot value).
     *
     * @return end parameter
     */
    public double endParameter() {
        return knotVector.end();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalBSplineCurve2 that = (RationalBSplineCurve2) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(weights, that.weights) && Objects.equals(knotVector.multiplicities(), that.knotVector.multiplicities()) && Objects.equals(knotVector.knots(), that.knotVector.knots());
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, weights, knotVector.multiplicities(), knotVector.knots());
    }

    @Override
    public String toString() {
        return "RationalBSplineCurve2{" + "degree=" + degree + "controlPoints=" + controlPoints + "weights=" + weights + "knotMultiplicities=" + knotVector.multiplicities() + "knots=" + knotVector.knots() + "}";
    }

    @Override
    public List<Point2> sample(int segments) {
        return BSplineKernel.sampleDomain(startParameter(), endParameter(), segments, this::pointAt);
    }
}
