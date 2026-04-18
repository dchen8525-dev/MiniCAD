package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal non-rational B-spline curve in 2D parameter space.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public record BSplineCurve2(
        int degree,
        List<Point2> controlPoints,
        List<Integer> knotMultiplicities,
        List<Double> knots
) implements Curve2 {

    public BSplineCurve2 {
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

    public Point2 pointAt(double parameter) {
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
        Point2[] d = new Point2[p + 1];
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

    public List<Point2> sample(int segments) {
        int effectiveSegments = Math.max(segments, 8);
        double start = startParameter();
        double end = endParameter();
        List<Point2> samples = new ArrayList<>(effectiveSegments + 1);
        for (int index = 0; index <= effectiveSegments; index++) {
            double u = start + (end - start) * index / effectiveSegments;
            samples.add(pointAt(u));
        }
        return List.copyOf(samples);
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        for (Point2 sample : sample(96)) {
            if (sample.subtract(point).norm() <= 1.0e-6) {
                return true;
            }
        }
        return false;
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

    private static Point2 interpolate(Point2 left, Point2 right, double alpha) {
        return new Point2(
                left.x() * (1.0 - alpha) + right.x() * alpha,
                left.y() * (1.0 - alpha) + right.y() * alpha
        );
    }

    /**
     * Returns the closest point on the curve to a given point.
     *
     * @param point target point
     * @return approximate closest point on the curve
     */
    @Override
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Point2 closest = null;
        double minDist = Double.POSITIVE_INFINITY;
        for (int resolution : new int[]{32, 64, 128}) {
            for (Point2 sample : sample(resolution)) {
                double d = sample.distanceTo(point);
                if (d < minDist) {
                    minDist = d;
                    closest = sample;
                }
            }
        }
        return closest != null ? closest : pointAt(startParameter());
    }

    /**
     * Returns the approximate arc length of the curve.
     *
     * @return arc length
     */
    @Override
    public double length() {
        List<Point2> samples = sample(128);
        double total = 0.0;
        for (int i = 1; i < samples.size(); i++) {
            total += samples.get(i).distanceTo(samples.get(i - 1));
        }
        return total;
    }

    /**
     * Computes the tangent vector at a given parameter.
     *
     * @param parameter parameter in the knot domain
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        double start = startParameter();
        double end = endParameter();
        double eps = (end - start) * 1e-6;
        eps = Math.max(eps, 1e-12);

        double u1 = Math.max(start, parameter - eps);
        double u2 = Math.min(end, parameter + eps);

        Point2 p1 = pointAt(u1);
        Point2 p2 = pointAt(u2);
        Vector2 tangent = p2.subtract(p1);

        if (tangent.isZero()) {
            return new Vector2(1, 0);
        }
        return tangent.normalize().asVector();
    }

    /**
     * Returns the approximate bounding box based on control points.
     *
     * @return bounding box enclosing all control points
     */
    public BoundingBox2 boundingBox() {
        return BoundingBox2.of(controlPoints);
    }

    /**
     * Returns a more accurate bounding box by sampling the curve.
     *
     * @param segments number of segments to sample
     * @return bounding box enclosing sampled curve points
     */
    public BoundingBox2 boundingBox(int segments) {
        return BoundingBox2.of(sample(segments));
    }

    /**
     * Returns the midpoint of the curve.
     *
     * @return midpoint
     */
    public Point2 midpoint() {
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
}
