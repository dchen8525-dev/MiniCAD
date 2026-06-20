package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
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
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;

    public RationalBSplineCurve2(int degree, List<Point2> controlPoints, List<Double> weights, List<Integer> knotMultiplicities, List<Double> knots) {
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.weights = weights == null ? null : java.util.List.copyOf(weights);
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
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
        return knotMultiplicities;
    }

    public List<Double> getKnots() {
        return knots;
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
        // Simple approximation using control points for now
        if (controlPoints == null || controlPoints.isEmpty()) {
            return new Point2(0, 0);
        }
        // Linear interpolation between control points as fallback
        double start = knots == null || knots.isEmpty() ? 0.0 : knots.get(0);
        double end = knots == null || knots.isEmpty() ? 1.0 : knots.get(knots.size() - 1);
        double t = (parameter - start) / (end - start);
        t = Math.max(0.0, Math.min(1.0, t));
        int n = controlPoints.size();
        if (n == 1) {
            return controlPoints.get(0);
        }
        int i = (int) (t * (n - 1));
        i = Math.max(0, Math.min(i, n - 2));
        double localT = t * (n - 1) - i;
        Point2 p0 = controlPoints.get(i);
        Point2 p1 = controlPoints.get(i + 1);
        return new Point2(p0.getX() + localT * (p1.getX() - p0.getX()), p0.getY() + localT * (p1.getY() - p0.getY()));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalBSplineCurve2 that = (RationalBSplineCurve2) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(weights, that.weights) && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, weights, knotMultiplicities, knots);
    }

    @Override
    public String toString() {
        return "RationalBSplineCurve2{" + "degree=" + degree + "controlPoints=" + controlPoints + "weights=" + weights + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "}";
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point is close to any sampled point
        List<Point2> samples = sample(64);
        for (Point2 sample : samples) {
            if (point.distanceTo(sample) < Epsilon.get()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Point2> sample(int segments) {
        List<Point2> points = new ArrayList<>();
        if (controlPoints == null || controlPoints.isEmpty()) {
            return List.copyOf(points);
        }
        double start = startParameter();
        double end = endParameter();
        for (int i = 0; i <= segments; i++) {
            double t = start + (end - start) * i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }
}
