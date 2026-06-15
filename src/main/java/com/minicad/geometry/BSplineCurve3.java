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

    public BSplineCurve3(int degree, List<CartesianPoint> controlPoints, List<Integer> knotMultiplicities, List<Double> knots) {
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
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

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Simple approximation using control points for now
        if (controlPoints == null || controlPoints.isEmpty()) {
            return CartesianPoint.origin();
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
        CartesianPoint p0 = controlPoints.get(i);
        CartesianPoint p1 = controlPoints.get(i + 1);
        return new CartesianPoint(
            p0.x() + localT * (p1.x() - p0.x()),
            p0.y() + localT * (p1.y() - p0.y()),
            p0.z() + localT * (p1.z() - p0.z())
        );
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
