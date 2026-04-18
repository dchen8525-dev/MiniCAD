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
    @Override
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
        return distanceTo(point) <= 1.0e-3;
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

    /**
     * Returns the closest point on the curve to a given point.
     * Uses sampling for initial guess then Newton-Raphson refinement.
     *
     * @param point target point
     * @return closest point on the curve
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        double start = startParameter();
        double end = endParameter();
        // Sampling-based initial guess
        java.util.List<CartesianPoint> samples = sample(256);
        int bestIndex = 0;
        double minDistance = point.distanceTo(samples.get(0));
        for (int i = 1; i < samples.size(); i++) {
            double distance = point.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                bestIndex = i;
            }
        }
        double t = start + (end - start) * bestIndex / (samples.size() - 1);
        // Newton-Raphson refinement: minimize ||C(t) - P||^2
        // Update: dt = -(C(t)-P) . C'(t) / |C'(t)|^2
        for (int iter = 0; iter < 20; iter++) {
            CartesianPoint cp = pointAt(t);
            Vector3 residual = cp.subtract(point);
            Vector3 deriv = tangentAt(t);
            double derivNormSq = deriv.normSquared();
            if (derivNormSq <= Epsilon.EPS) break;
            double dt = -residual.dot(deriv) / derivNormSq;
            t = Math.max(start, Math.min(end, t + dt));
            if (Math.abs(dt) < 1e-12) break;
        }
        return pointAt(t);
    }

    /**
     * Returns the distance from a point to the curve.
     *
     * @param point target point
     * @return minimum distance to the curve
     */
    @Override
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
     * Returns the curve parameter corresponding to the given point.
     * Uses sampling for initial guess then Newton-Raphson refinement.
     *
     * @param point a point on or near the curve
     * @return parameter value in knot-space [startParameter, endParameter]
     */
    @Override
    public double parameterAt(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        double start = startParameter();
        double end = endParameter();
        // Sampling-based initial guess
        java.util.List<CartesianPoint> samples = sample(256);
        int bestIndex = 0;
        double minDistance = point.distanceTo(samples.get(0));
        for (int i = 1; i < samples.size(); i++) {
            double distance = point.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                bestIndex = i;
            }
        }
        double t = start + (end - start) * bestIndex / (samples.size() - 1);
        // Newton-Raphson refinement: minimize ||C(t) - P||^2
        for (int iter = 0; iter < 20; iter++) {
            CartesianPoint cp = pointAt(t);
            Vector3 residual = cp.subtract(point);
            Vector3 deriv = tangentAt(t);
            double derivNormSq = deriv.normSquared();
            if (derivNormSq <= Epsilon.EPS) break;
            double dt = -residual.dot(deriv) / derivNormSq;
            t = Math.max(start, Math.min(end, t + dt));
            if (Math.abs(dt) < 1e-12) break;
        }
        return t;
    }
}
