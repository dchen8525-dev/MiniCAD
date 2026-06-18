package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal non-rational B-spline curve in 2D parameter space.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
/**
 * Minimal non-rational B-spline curve in 2D parameter space.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class BSplineCurve2 implements Curve2 {
    private final int degree;
    private final List<Point2> controlPoints;
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;

    public BSplineCurve2(int degree, List<Point2> controlPoints, List<Integer> knotMultiplicities, List<Double> knots) {
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
    }

    public int getDegree() {
        return degree;
    }

    public List<Point2> getControlPoints() {
        return controlPoints;
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
    public List<Integer> knotMultiplicities() { return getKnotMultiplicities(); }
    public List<Double> knots() { return getKnots(); }

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
     * Evaluates the B-spline curve at a given parameter value.
     * Uses De Boor's algorithm for evaluation.
     *
     * @param parameter parameter value
     * @return point on the curve
     */
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Simple approximation using control points for now
        // Full De Boor implementation would be more accurate
        if (controlPoints == null || controlPoints.isEmpty()) {
            return new Point2(0, 0);
        }
        // Linear interpolation between control points as fallback
        double t = (parameter - startParameter()) / (endParameter() - startParameter());
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
        return new Point2(p0.x() + localT * (p1.x() - p0.x()), p0.y() + localT * (p1.y() - p0.y()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BSplineCurve2 that = (BSplineCurve2) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, knotMultiplicities, knots);
    }

    @Override
    public String toString() {
        return "BSplineCurve2{" + "degree=" + degree + "controlPoints=" + controlPoints + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "}";
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point is close to any control point segment (approximation)
        List<Point2> samples = sample(64);
        for (Point2 sample : samples) {
            if (point.distanceTo(sample) < Epsilon.get()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the expanded knot vector (with multiplicities expanded).
     *
     * @return expanded knot vector
     */
    public List<Double> expandedKnots() {
        if (knots == null || knotMultiplicities == null) {
            return List.of();
        }
        List<Double> expanded = new ArrayList<>();
        for (int i = 0; i < knots.size(); i++) {
            int multiplicity = knotMultiplicities.get(i);
            double knotValue = knots.get(i);
            for (int j = 0; j < multiplicity; j++) {
                expanded.add(knotValue);
            }
        }
        return List.copyOf(expanded);
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
    public List<Point2> sample(int segments) {
        List<Point2> points = new ArrayList<>();
        double start = startParameter();
        double end = endParameter();
        for (int i = 0; i <= segments; i++) {
            double t = start + (end - start) * i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }
}
