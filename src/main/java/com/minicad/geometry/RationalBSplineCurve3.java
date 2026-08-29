package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal rational B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param weights weights for control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
/**
 * Minimal rational B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param weights weights for control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class RationalBSplineCurve3 implements Curve3 {
    private final int degree;
    private final List<CartesianPoint> controlPoints;
    private final List<Double> weights;
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;

    private volatile List<Double> expandedKnotsCache;

    public RationalBSplineCurve3(int degree, List<CartesianPoint> controlPoints, List<Double> weights, List<Integer> knotMultiplicities, List<Double> knots) {
        BSplineCurve3.validateDefinition(degree, controlPoints, knotMultiplicities, knots);
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
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
    }

    public int getDegree() {
        return degree;
    }

    public List<CartesianPoint> getControlPoints() {
        return controlPoints;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public List<Integer> getKnotMultiplicities() {
        return knotMultiplicities;
    }

    public List<Double> getKnots() {
        return knots;
    }

    // Record-style accessors
    public int degree() { return degree; }
    public List<CartesianPoint> controlPoints() { return controlPoints; }
    public List<Double> weights() { return weights; }
    public List<Integer> knotMultiplicities() { return knotMultiplicities; }
    public List<Double> knots() { return knots; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalBSplineCurve3 that = (RationalBSplineCurve3) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(weights, that.weights) && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, weights, knotMultiplicities, knots);
    }

    @Override
    public String toString() {
        return "RationalBSplineCurve3{" + "degree=" + degree + "controlPoints=" + controlPoints + "weights=" + weights + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "}";
    }

    /**
     * Returns the start parameter of the curve (first knot value).
     *
     * @return start parameter
     */
    public double startParameter() {
        if (knots == null || knots.isEmpty()) {
            return 0.0;
        }
        return knots.get(0);
    }

    /**
     * Returns the end parameter of the curve (last knot value).
     *
     * @return end parameter
     */
    public double endParameter() {
        if (knots == null || knots.isEmpty()) {
            return 1.0;
        }
        return knots.get(knots.size() - 1);
    }

    /**
     * Returns the expanded knot vector (with multiplicities expanded).
     * Cached after first use to avoid repeated allocation on evaluation hot paths.
     *
     * @return expanded knot vector
     */
    public List<Double> expandedKnots() {
        List<Double> local = expandedKnotsCache;
        if (local == null) {
            local = computeExpandedKnots();
            expandedKnotsCache = local;
        }
        return local;
    }

    private List<Double> computeExpandedKnots() {
        if (knots == null || knotMultiplicities == null) {
            return java.util.List.of();
        }
        List<Double> expanded = new ArrayList<>();
        for (int i = 0; i < knots.size(); i++) {
            int multiplicity = knotMultiplicities.get(i);
            double knotValue = knots.get(i);
            for (int j = 0; j < multiplicity; j++) {
                expanded.add(knotValue);
            }
        }
        return java.util.List.copyOf(expanded);
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (controlPoints == null || controlPoints.isEmpty() || weights == null) {
            return CartesianPoint.origin();
        }
        List<Double> expanded = expandedKnots();
        if (expanded.size() <= degree + 1) {
            return CartesianPoint.origin();
        }
        return BSplineMath.evaluateRational(controlPoints, weights, degree, parameter, expanded);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        java.util.List<CartesianPoint> samples = sample(64);
        for (CartesianPoint sample : samples) {
            if (point.distanceTo(sample) < Epsilon.get()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        java.util.List<CartesianPoint> samples = sample(256);
        CartesianPoint closest = samples.get(0);
        double minDist = point.distanceTo(closest);
        for (int i = 1; i < samples.size(); i++) {
            double dist = point.distanceTo(samples.get(i));
            if (dist < minDist) {
                minDist = dist;
                closest = samples.get(i);
            }
        }
        return closest;
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        if (controlPoints == null || controlPoints.isEmpty()) {
            return java.util.List.copyOf(points);
        }
        double start = startParameter();
        double end = endParameter();
        for (int i = 0; i <= segments; i++) {
            double t = start + (end - start) * i / segments;
            points.add(pointAt(t));
        }
        return java.util.List.copyOf(points);
    }
}
