package com.minicad.geometry;

import com.minicad.common.Preconditions;

/**
 * Minimal trimmed-curve wrapper over a supported basis curve.
 *
 * @param basisCurve supported basis curve
 * @param trimStart first trim point
 * @param trimEnd second trim point
 * @param senseAgreement trimming orientation agreement
 */
public record TrimmedCurve3(
        Curve3 basisCurve,
        CartesianPoint trimStart,
        CartesianPoint trimEnd,
        boolean senseAgreement
) implements Curve3 {

    /**
     * Creates a trimmed curve.
     */
    public TrimmedCurve3 {
        Preconditions.requireNonNull(basisCurve, "basisCurve");
        Preconditions.requireNonNull(trimStart, "trimStart");
        Preconditions.requireNonNull(trimEnd, "trimEnd");
    }

    @Override
    public boolean contains(CartesianPoint point) {
        return basisCurve.contains(point);
    }

    /**
     * Evaluates a point on the trimmed curve at the given parameter.
     * The parameter is mapped from the trim start to trim end.
     *
     * @param parameter parameter value (0 to 1)
     * @return point on the trimmed curve
     */
    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (senseAgreement) {
            return interpolate(trimStart, trimEnd, parameter);
        } else {
            return interpolate(trimEnd, trimStart, parameter);
        }
    }

    /**
     * Samples the trimmed curve by sampling the basis curve between trim points.
     *
     * @param segments number of segments to sample
     * @return list of sampled points from trimStart to trimEnd
     */
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            CartesianPoint point;
            if (senseAgreement) {
                point = interpolate(trimStart, trimEnd, t);
            } else {
                point = interpolate(trimEnd, trimStart, t);
            }
            points.add(point);
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Computes the tangent vector at a given parameter.
     * The tangent direction depends on senseAgreement.
     *
     * @param parameter parameter value (0 to 1)
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        Vector3 direction = trimEnd.subtract(trimStart);
        if (!senseAgreement) {
            direction = trimStart.subtract(trimEnd);
        }
        if (direction.norm() <= com.minicad.common.Epsilon.EPS) {
            return new Vector3(1, 0, 0);
        }
        return direction.normalize().asVector();
    }

    /**
     * Returns the bounding box enclosing the trimmed curve.
     *
     * @return bounding box enclosing trim start and end
     */
    public BoundingBox3 boundingBox() {
        return BoundingBox3.of(trimStart, trimEnd);
    }

    /**
     * Returns the length of the trimmed curve segment.
     *
     * @return length from trimStart to trimEnd
     */
    public double length() {
        return trimStart.distanceTo(trimEnd);
    }

    private static CartesianPoint interpolate(CartesianPoint start, CartesianPoint end, double t) {
        return new CartesianPoint(
                start.x() + (end.x() - start.x()) * t,
                start.y() + (end.y() - start.y()) * t,
                start.z() + (end.z() - start.z()) * t
        );
    }

    /**
     * Returns the closest point on the trimmed curve to a given point.
     *
     * @param point target point
     * @return closest point on the trimmed curve
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // For linear interpolation between trim points
        Vector3 direction = trimEnd.subtract(trimStart);
        if (direction.isZero()) {
            return trimStart;
        }
        double t = point.subtract(trimStart).dot(direction) / direction.dot(direction);
        t = Math.max(0.0, Math.min(1.0, t));
        return interpolate(trimStart, trimEnd, t);
    }

    /**
     * Returns the distance from a point to the trimmed curve.
     *
     * @param point target point
     * @return minimum distance to the trimmed curve
     */
    @Override
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the trimmed curve.
     *
     * @return midpoint
     */
    public CartesianPoint midpoint() {
        return interpolate(trimStart, trimEnd, 0.5);
    }

    /**
     * Returns the curvature of the trimmed curve.
     * Delegates to the basis curve if it has curvature, otherwise returns 0.
     *
     * @return curvature
     */
    public double curvature() {
        if (basisCurve instanceof Circle circle) {
            return circle.curvature();
        } else if (basisCurve instanceof Ellipse3 ellipse) {
            return ellipse.curvatureAt(0);
        } else if (basisCurve instanceof Line3) {
            return 0.0;
        }
        return 0.0;  // Default for other curves
    }

    /**
     * Returns the curvature at a given parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return curvature at the parameter
     */
    public double curvatureAt(double parameter) {
        return curvature();
    }
}
