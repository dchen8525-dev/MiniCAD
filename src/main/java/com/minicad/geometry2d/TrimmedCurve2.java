package com.minicad.geometry2d;

import com.minicad.common.Preconditions;

/**
 * Minimal 2D trimmed curve wrapper for parameter-space curves.
 *
 * @param basisCurve underlying supported 2D curve
 * @param trimStart first trim point
 * @param trimEnd second trim point
 * @param senseAgreement trimming orientation agreement
 */
public record TrimmedCurve2(
        Curve2 basisCurve,
        Point2 trimStart,
        Point2 trimEnd,
        boolean senseAgreement
) implements Curve2 {

    public TrimmedCurve2 {
        Preconditions.requireNonNull(basisCurve, "basisCurve");
        Preconditions.requireNonNull(trimStart, "trimStart");
        Preconditions.requireNonNull(trimEnd, "trimEnd");
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return basisCurve.contains(point);
    }

    /**
     * Samples the trimmed curve between trim points.
     *
     * @param segments number of segments to sample
     * @return list of sampled points from trimStart to trimEnd
     */
    public java.util.List<Point2> sample(int segments) {
        java.util.List<Point2> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            points.add(pointAt(t));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Evaluates a point on the trimmed curve at the given parameter.
     * The parameter is mapped from the trim start to trim end.
     *
     * @param parameter parameter value (0 to 1)
     * @return point on the trimmed curve
     */
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (senseAgreement) {
            return interpolate(trimStart, trimEnd, parameter);
        } else {
            return interpolate(trimEnd, trimStart, parameter);
        }
    }

    private static Point2 interpolate(Point2 start, Point2 end, double t) {
        return new Point2(
                start.x() + (end.x() - start.x()) * t,
                start.y() + (end.y() - start.y()) * t
        );
    }

    /**
     * Returns the bounding box enclosing the trimmed curve.
     *
     * @return bounding box enclosing trim start and end
     */
    public BoundingBox2 boundingBox() {
        return BoundingBox2.of(trimStart, trimEnd);
    }

    /**
     * Returns the length of the trimmed curve segment.
     *
     * @return length from trimStart to trimEnd
     */
    public double length() {
        return trimStart.distanceTo(trimEnd);
    }

    /**
     * Returns the tangent vector at a given parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        Vector2 direction = trimEnd.subtract(trimStart);
        if (!senseAgreement) {
            direction = trimStart.subtract(trimEnd);
        }
        if (direction.isZero()) {
            return new Vector2(1, 0);
        }
        return direction.normalize().asVector();
    }

    /**
     * Returns the closest point on the trimmed curve to a given point.
     *
     * @param point target point
     * @return closest point on the trimmed curve
     */
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 direction = trimEnd.subtract(trimStart);
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
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the trimmed curve.
     *
     * @return midpoint
     */
    public Point2 midpoint() {
        return interpolate(trimStart, trimEnd, 0.5);
    }

    /**
     * Returns the underlying curve.
     *
     * @return basis curve
     */
    public Curve2 underlyingCurve() {
        return basisCurve;
    }
}
