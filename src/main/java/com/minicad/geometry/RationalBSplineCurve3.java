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

    /**
     * Computes the tangent vector at a given parameter.
     * Uses numerical differentiation for simplicity.
     *
     * @param parameter parameter in the knot domain
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        double start = startParameter();
        double end = endParameter();
        double eps = (end - start) * 1e-6;
        eps = Math.max(eps, 1e-12);

        double u1 = Math.max(start, parameter - eps);
        double u2 = Math.min(end, parameter + eps);

        CartesianPoint p1 = pointAt(u1);
        CartesianPoint p2 = pointAt(u2);
        Vector3 tangent = p2.subtract(p1);

        if (tangent.norm() <= Epsilon.EPS) {
            // Degenerate case, use a larger epsilon
            u1 = Math.max(start, parameter - 1e-3);
            u2 = Math.min(end, parameter + 1e-3);
            p1 = pointAt(u1);
            p2 = pointAt(u2);
            tangent = p2.subtract(p1);
        }

        if (tangent.norm() <= Epsilon.EPS) {
            // Still degenerate, return a default direction
            return new Vector3(1, 0, 0);
        }
        return tangent.normalize().asVector();
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

    /**
     * Returns the approximate bounding box based on control points.
     * This is a conservative bound that may be larger than the actual curve extent.
     *
     * @return bounding box enclosing all control points
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        for (CartesianPoint point : controlPoints) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Returns a more accurate bounding box by sampling the curve.
     *
     * @param segments number of segments to sample
     * @return bounding box enclosing sampled curve points
     */
    public BoundingBox3 boundingBox(int segments) {
        BoundingBox3 box = BoundingBox3.empty();
        List<CartesianPoint> samples = sample(segments);
        for (CartesianPoint point : samples) {
            box = box.union(point);
        }
        return box;
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

    /**
     * Returns the approximate length of the curve.
     * Computed by summing distances between sampled points.
     *
     * @return approximate curve length
     */
    public double length() {
        List<CartesianPoint> samples = sample(256);
        double totalLength = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            totalLength += samples.get(i).distanceTo(samples.get(i + 1));
        }
        return totalLength;
    }

    /**
     * Returns the closest point on the curve to a given point.
     *
     * @param point target point
     * @return closest point on the curve
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        List<CartesianPoint> samples = sample(256);
        CartesianPoint closest = samples.get(0);
        double minDistance = point.distanceTo(closest);
        for (int i = 1; i < samples.size(); i++) {
            double distance = point.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                closest = samples.get(i);
            }
        }
        return closest;
    }

    /**
     * Returns the distance from a point to the curve.
     *
     * @param point target point
     * @return minimum distance to the curve
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the curve.
     *
     * @return midpoint
     */
    public CartesianPoint midpoint() {
        double midParam = (startParameter() + endParameter()) / 2.0;
        return pointAt(midParam);
    }

    /**
     * Returns the control point count.
     *
     * @return number of control points
     */
    public int controlPointCount() {
        return controlPoints.size();
    }

    /**
     * Returns the knot count (unique knots).
     *
     * @return number of unique knots
     */
    public int knotCount() {
        return knots.size();
    }

    /**
     * Returns the weight count.
     *
     * @return number of weights
     */
    public int weightCount() {
        return weights.size();
    }
}
