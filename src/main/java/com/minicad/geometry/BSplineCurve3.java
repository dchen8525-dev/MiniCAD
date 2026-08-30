package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal non-rational B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
/**
 * Minimal non-rational B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class BSplineCurve3 implements Curve3 {
    private final int degree;
    private final List<CartesianPoint> controlPoints;
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;

    private volatile List<Double> expandedKnotsCache;

    public BSplineCurve3(int degree, List<CartesianPoint> controlPoints, List<Integer> knotMultiplicities, List<Double> knots) {
        validateDefinition(degree, controlPoints, knotMultiplicities, knots);
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
    }

    static void validateDefinition(int degree, List<CartesianPoint> controlPoints,
                                   List<Integer> knotMultiplicities, List<Double> knots) {
        if (degree < 1) {
            throw new GeometryException("B-spline degree must be positive");
        }
        if (controlPoints == null || controlPoints.size() <= degree) {
            throw new GeometryException("B-spline requires more control points than its degree");
        }
        if (knots == null || knotMultiplicities == null || knots.size() != knotMultiplicities.size()) {
            throw new GeometryException("knot values and multiplicities must have equal size");
        }
        int expandedCount = 0;
        double previous = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < knots.size(); i++) {
            double knot = knots.get(i);
            int multiplicity = knotMultiplicities.get(i);
            if (!Double.isFinite(knot) || knot <= previous) {
                throw new GeometryException("knot values must be finite and strictly increasing");
            }
            if (multiplicity <= 0) {
                throw new GeometryException("knot multiplicities must be positive");
            }
            previous = knot;
            expandedCount += multiplicity;
        }
        if (expandedCount != controlPoints.size() + degree + 1) {
            throw new GeometryException("expanded knot count does not match control points and degree");
        }
    }

    public int getDegree() {
        return degree;
    }

    public List<CartesianPoint> getControlPoints() {
        return controlPoints;
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
    public List<Integer> knotMultiplicities() { return knotMultiplicities; }
    public List<Double> knots() { return knots; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BSplineCurve3 that = (BSplineCurve3) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, knotMultiplicities, knots);
    }

    @Override
    public String toString() {
        return "BSplineCurve3{" + "degree=" + degree + "controlPoints=" + controlPoints + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "}";
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

    /**
     * Returns the midpoint of the curve (point at middle parameter).
     *
     * @return midpoint
     */
    public CartesianPoint midpoint() {
        double midParam = (startParameter() + endParameter()) / 2.0;
        return pointAt(midParam);
    }

    /**
     * Returns the number of control points.
     *
     * @return control point count
     */
    public int controlPointCount() {
        return controlPoints == null ? 0 : controlPoints.size();
    }

    /**
     * Returns the number of unique knots.
     *
     * @return knot count
     */
    public int knotCount() {
        return knots == null ? 0 : knots.size();
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (controlPoints == null || controlPoints.isEmpty()) {
            return CartesianPoint.origin();
        }
        List<Double> expanded = expandedKnots();
        if (expanded.size() <= degree + 1) {
            return CartesianPoint.origin();
        }
        return BSplineMath.evaluate(controlPoints, degree, parameter, expanded);
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
    public double parameterAt(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        int samples = 1024;
        double start = startParameter();
        double end = endParameter();
        double bestParameter = start;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i <= samples; i++) {
            double parameter = start + (end - start) * i / samples;
            double distance = point.distanceTo(pointAt(parameter));
            if (distance < bestDistance) {
                bestDistance = distance;
                bestParameter = parameter;
            }
        }
        // The coarse scan quantizes to domain/1024; polish the winner locally
        // so Edge sampling lands its endpoint parameters on the true vertices.
        // Keep the coarse winner when refinement does not improve on it - the
        // minimum may sit exactly on a bracket boundary (e.g. an endpoint hit).
        double step = (end - start) / samples;
        double refined = BSplineMath.refineLocalMinimum(
                p -> point.distanceTo(pointAt(p)),
                Math.max(start, bestParameter - step),
                Math.min(end, bestParameter + step),
                40);
        return point.distanceTo(pointAt(refined)) <= bestDistance ? refined : bestParameter;
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        if (controlPoints == null || controlPoints.isEmpty()) {
            return java.util.List.copyOf(points);
        }
        segments = Math.max(8, segments);
        double start = startParameter();
        double end = endParameter();
        for (int i = 0; i <= segments; i++) {
            double t = start + (end - start) * i / segments;
            points.add(pointAt(t));
        }
        return java.util.List.copyOf(points);
    }
}
