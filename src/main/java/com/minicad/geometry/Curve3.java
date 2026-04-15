package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Minimal curve abstraction for topology-backed geometry.
 */
public sealed interface Curve3 permits
        Line3,
        Circle,
        Ellipse3,
        Parabola3,
        Hyperbola3,
        Clothoid3,
        DegenerateCurve3,
        BSplineCurve3,
        RationalBSplineCurve3,
        TrimmedCurve3,
        SurfaceCurve3,
        Polyline3,
        CompositeCurve3 {

    /**
     * Returns whether a point lies on the curve within epsilon.
     *
     * @param point queried point
     * @return whether the point lies on the curve
     */
    boolean contains(CartesianPoint point);

    /**
     * Returns the approximate bounding box of the curve by sampling.
     * Implementations may override with more efficient calculations.
     *
     * @return bounding box enclosing the curve
     */
    default BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        java.util.List<CartesianPoint> samples = sample(64);
        for (CartesianPoint point : samples) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Samples the curve at discrete points.
     * Implementations may override with curve-specific sampling logic.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    default java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        // Default: can't sample without knowing curve type
        return java.util.List.copyOf(points);
    }

    /**
     * Returns the approximate length of the curve by sampling.
     * Implementations may override with more accurate calculations.
     *
     * @return approximate length
     */
    default double length() {
        java.util.List<CartesianPoint> samples = sample(128);
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
    default Vector3 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        double eps = 0.001;
        java.util.List<CartesianPoint> samples = sample(256);
        if (samples.size() < 2) {
            return new Vector3(1, 0, 0);
        }
        // Map parameter to index in samples
        int index = (int) (parameter * (samples.size() - 1));
        index = Math.max(0, Math.min(index, samples.size() - 2));
        Vector3 tangent = samples.get(index + 1).subtract(samples.get(index));
        if (tangent.norm() <= Epsilon.EPS) {
            return new Vector3(1, 0, 0);
        }
        return tangent.normalize().asVector();
    }
}
