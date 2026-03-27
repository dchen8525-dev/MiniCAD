package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal non-rational B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public record BSplineCurve3(
        int degree,
        List<CartesianPoint> controlPoints,
        List<Integer> knotMultiplicities,
        List<Double> knots
) implements Curve3 {

    /**
     * Creates a B-spline curve and validates basic invariants.
     */
    public BSplineCurve3 {
        if (degree < 1) {
            throw new GeometryException("degree must be at least 1");
        }
        controlPoints = List.copyOf(controlPoints);
        knotMultiplicities = List.copyOf(knotMultiplicities);
        knots = List.copyOf(knots);
        if (controlPoints.size() < degree + 1) {
            throw new GeometryException("control point count must be at least degree + 1");
        }
        if (knotMultiplicities.size() != knots.size()) {
            throw new GeometryException("knot multiplicities and knots must have the same size");
        }
    }

    /**
     * Returns the expanded knot vector.
     *
     * @return expanded knot vector
     */
    public List<Double> expandedKnots() {
        List<Double> expanded = new ArrayList<>();
        for (int index = 0; index < knots.size(); index++) {
            double knot = knots.get(index);
            int multiplicity = knotMultiplicities.get(index);
            for (int repeat = 0; repeat < multiplicity; repeat++) {
                expanded.add(knot);
            }
        }
        return List.copyOf(expanded);
    }

    /**
     * Returns the start parameter.
     *
     * @return start parameter
     */
    public double startParameter() {
        List<Double> expanded = expandedKnots();
        return expanded.get(degree);
    }

    /**
     * Returns the end parameter.
     *
     * @return end parameter
     */
    public double endParameter() {
        List<Double> expanded = expandedKnots();
        return expanded.get(expanded.size() - degree - 1);
    }

    /**
     * Evaluates the curve at the given parameter.
     *
     * @param parameter parameter in the knot domain
     * @return evaluated point
     */
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        List<Double> expanded = expandedKnots();
        int n = controlPoints.size() - 1;
        int p = degree;
        double low = expanded.get(p);
        double high = expanded.get(n + 1);
        double u = Math.max(low, Math.min(parameter, high));
        if (Epsilon.equals(u, high)) {
            return controlPoints.getLast();
        }

        int span = findSpan(n, p, u, expanded);
        CartesianPoint[] d = new CartesianPoint[p + 1];
        for (int j = 0; j <= p; j++) {
            d[j] = controlPoints.get(span - p + j);
        }
        for (int r = 1; r <= p; r++) {
            for (int j = p; j >= r; j--) {
                int knotIndex = span - p + j;
                double denominator = expanded.get(knotIndex + p - r + 1) - expanded.get(knotIndex);
                double alpha = Epsilon.isZero(denominator) ? 0.0 : (u - expanded.get(knotIndex)) / denominator;
                d[j] = interpolate(d[j - 1], d[j], alpha);
            }
        }
        return d[p];
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        List<CartesianPoint> samples = sample(96);
        for (CartesianPoint sample : samples) {
            if (sample.distanceTo(point) <= 1.0e-6) {
                return true;
            }
        }
        return false;
    }

    /**
     * Samples the curve with the given number of segments.
     *
     * @param segments number of segments
     * @return sampled points including endpoints
     */
    public List<CartesianPoint> sample(int segments) {
        int effectiveSegments = Math.max(segments, 8);
        double start = startParameter();
        double end = endParameter();
        List<CartesianPoint> samples = new ArrayList<>(effectiveSegments + 1);
        for (int index = 0; index <= effectiveSegments; index++) {
            double u = start + (end - start) * index / effectiveSegments;
            samples.add(pointAt(u));
        }
        return List.copyOf(samples);
    }

    private static int findSpan(int n, int p, double u, List<Double> expandedKnots) {
        if (u >= expandedKnots.get(n + 1)) {
            return n;
        }
        int low = p;
        int high = n + 1;
        int mid = (low + high) / 2;
        while (u < expandedKnots.get(mid) || u >= expandedKnots.get(mid + 1)) {
            if (u < expandedKnots.get(mid)) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }

    private static CartesianPoint interpolate(CartesianPoint left, CartesianPoint right, double alpha) {
        return new CartesianPoint(
                left.x() * (1.0 - alpha) + right.x() * alpha,
                left.y() * (1.0 - alpha) + right.y() * alpha,
                left.z() * (1.0 - alpha) + right.z() * alpha
        );
    }
}
