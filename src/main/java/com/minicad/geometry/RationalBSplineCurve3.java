package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal rational B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param weights weights for control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public record RationalBSplineCurve3(
        int degree,
        List<CartesianPoint> controlPoints,
        List<Double> weights,
        List<Integer> knotMultiplicities,
        List<Double> knots
) implements Curve3 {

    public RationalBSplineCurve3 {
        if (degree < 1) {
            throw new GeometryException("degree must be at least 1");
        }
        controlPoints = List.copyOf(controlPoints);
        weights = List.copyOf(weights);
        knotMultiplicities = List.copyOf(knotMultiplicities);
        knots = List.copyOf(knots);
        if (controlPoints.size() < degree + 1) {
            throw new GeometryException("control point count must be at least degree + 1");
        }
        if (weights.size() != controlPoints.size()) {
            throw new GeometryException("weights must match control points");
        }
        if (knotMultiplicities.size() != knots.size()) {
            throw new GeometryException("knot multiplicities and knots must have the same size");
        }
    }

    public List<Double> expandedKnots() {
        List<Double> expanded = new ArrayList<>();
        for (int index = 0; index < knots.size(); index++) {
            for (int repeat = 0; repeat < knotMultiplicities.get(index); repeat++) {
                expanded.add(knots.get(index));
            }
        }
        return List.copyOf(expanded);
    }

    public double startParameter() {
        List<Double> expanded = expandedKnots();
        return expanded.get(degree);
    }

    public double endParameter() {
        List<Double> expanded = expandedKnots();
        return expanded.get(expanded.size() - degree - 1);
    }

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
        double denominator = 0.0;
        Vector3 numerator = new Vector3(0.0, 0.0, 0.0);
        for (int i = 0; i <= p; i++) {
            int controlIndex = span - p + i;
            double basis = basisValue(controlIndex, p, u, expanded);
            double weightedBasis = basis * weights.get(controlIndex);
            CartesianPoint control = controlPoints.get(controlIndex);
            numerator = numerator.add(new Vector3(
                    control.x() * weightedBasis,
                    control.y() * weightedBasis,
                    control.z() * weightedBasis
            ));
            denominator += weightedBasis;
        }
        if (Epsilon.isZero(denominator)) {
            throw new GeometryException("rational curve denominator is zero");
        }
        return new CartesianPoint(
                numerator.x() / denominator,
                numerator.y() / denominator,
                numerator.z() / denominator
        );
    }

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

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        for (CartesianPoint sample : sample(256)) {
            if (sample.distanceTo(point) <= 1.0e-6) {
                return true;
            }
        }
        return false;
    }

    private static int findSpan(int n, int p, double u, List<Double> knots) {
        if (u >= knots.get(n + 1)) {
            return n;
        }
        int low = p;
        int high = n + 1;
        int mid = (low + high) / 2;
        while (u < knots.get(mid) || u >= knots.get(mid + 1)) {
            if (u < knots.get(mid)) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }

    private static double basisValue(int i, int degree, double parameter, List<Double> knots) {
        if (degree == 0) {
            if ((parameter >= knots.get(i) && parameter < knots.get(i + 1))
                    || (Epsilon.equals(parameter, knots.getLast()) && Epsilon.equals(parameter, knots.get(i + 1)))) {
                return 1.0;
            }
            return 0.0;
        }
        double leftDenominator = knots.get(i + degree) - knots.get(i);
        double rightDenominator = knots.get(i + degree + 1) - knots.get(i + 1);
        double left = Epsilon.isZero(leftDenominator)
                ? 0.0
                : (parameter - knots.get(i)) / leftDenominator * basisValue(i, degree - 1, parameter, knots);
        double right = Epsilon.isZero(rightDenominator)
                ? 0.0
                : (knots.get(i + degree + 1) - parameter) / rightDenominator * basisValue(i + 1, degree - 1, parameter, knots);
        return left + right;
    }
}
