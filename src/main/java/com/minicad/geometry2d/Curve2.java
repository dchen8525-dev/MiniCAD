package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal 2D curve abstraction for surface parameter space.
 */
public interface Curve2 {

    /**
     * Returns whether a point lies on the curve within epsilon.
     *
     * @param point queried point
     * @return whether the point lies on the curve
     */
    boolean contains(Point2 point);

    /**
     * Returns the approximate bounding box of the curve by sampling.
     * Implementations may override with more efficient calculations.
     *
     * @return bounding box enclosing the curve
     */
    default BoundingBox2 boundingBox() {
        BoundingBox2 box = BoundingBox2.empty();
        List<Point2> samples = sample(64);
        for (Point2 point : samples) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Samples the curve at discrete points.
     * Implementations should override with curve-specific sampling logic.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    default List<Point2> sample(int segments) {
        List<Point2> points = new ArrayList<>();
        return List.copyOf(points);
    }

    /**
     * Returns the approximate length of the curve by sampling.
     * Implementations may override with more accurate calculations.
     *
     * @return approximate length
     */
    default double length() {
        List<Point2> samples = sample(128);
        double totalLength = 0.0;
        for (int i = 0; i < samples.size() - 1; i++) {
            totalLength += samples.get(i).distanceTo(samples.get(i + 1));
        }
        return totalLength;
    }

    /**
     * Returns the tangent vector at a parameter value.
     * Default implementation uses numerical differentiation.
     *
     * @param parameter parameter value (interpretation depends on curve type)
     * @return unit tangent vector
     */
    default Vector2 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        double eps = 0.001;
        List<Point2> samples = sample(256);
        if (samples.size() < 2) {
            return new Vector2(1, 0);
        }
        // Map parameter to index in samples
        int index = (int) (parameter * (samples.size() - 1));
        index = Math.max(0, Math.min(index, samples.size() - 2));
        Vector2 tangent = samples.get(index + 1).subtract(samples.get(index));
        if (tangent.norm() <= Epsilon.EPS) {
            return new Vector2(1, 0);
        }
        return tangent.normalize().asVector();
    }

    /**
     * Returns the closest point on the curve to a given point.
     * Default implementation finds closest by sampling.
     *
     * @param point target point
     * @return closest point on the curve
     */
    default Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        List<Point2> samples = sample(256);
        Point2 closest = samples.get(0);
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
    default double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the curve by parameter.
     *
     * @return midpoint
     */
    default Point2 midpoint() {
        List<Point2> samples = sample(128);
        if (samples.isEmpty()) {
            return new Point2(0, 0);
        }
        return samples.get(samples.size() / 2);
    }
}
