package com.minicad.geometry;

import com.minicad.common.Preconditions;

/**
 * Immutable 3D Cartesian point.
 *
 * @param x x coordinate
 * @param y y coordinate
 * @param z z coordinate
 */
public record CartesianPoint(double x, double y, double z) {

    /**
     * Creates a point and validates its coordinates.
     *
     */
    public CartesianPoint {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
        Preconditions.requireFinite(z, "z");
    }

    /**
     * Returns the vector from {@code other} to this point.
     *
     * @param other start point
     * @return displacement vector
     */
    public Vector3 subtract(CartesianPoint other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Translates this point by a vector.
     *
     * @param vector translation vector
     * @return translated point
     */
    public CartesianPoint add(Vector3 vector) {
        Preconditions.requireNonNull(vector, "vector");
        return new CartesianPoint(x + vector.x(), y + vector.y(), z + vector.z());
    }

    /**
     * Subtracts a vector from this point.
     *
     * @param vector vector to subtract
     * @return new point displaced by the negative vector
     */
    public CartesianPoint subtractVector(Vector3 vector) {
        Preconditions.requireNonNull(vector, "vector");
        return new CartesianPoint(x - vector.x(), y - vector.y(), z - vector.z());
    }

    /**
     * Computes the Euclidean distance to another point.
     *
     * @param other target point
     * @return Euclidean distance
     */
    public double distanceTo(CartesianPoint other) {
        return subtract(other).norm();
    }

    /**
     * Returns the midpoint between this point and another point.
     *
     * @param other other point
     * @return midpoint
     */
    public CartesianPoint midpoint(CartesianPoint other) {
        Preconditions.requireNonNull(other, "other");
        return new CartesianPoint(
            (x + other.x) / 2.0,
            (y + other.y) / 2.0,
            (z + other.z) / 2.0
        );
    }

    /**
     * Interpolates between this point and another point.
     *
     * @param other target point
     * @param t interpolation parameter (0 = this point, 1 = other point)
     * @return interpolated point
     */
    public CartesianPoint interpolate(CartesianPoint other, double t) {
        Preconditions.requireNonNull(other, "other");
        Preconditions.requireFinite(t, "t");
        return new CartesianPoint(
            x + (other.x - x) * t,
            y + (other.y - y) * t,
            z + (other.z - z) * t
        );
    }

    /**
     * Projects this point onto a line.
     *
     * @param line target line
     * @return projected point on the line
     */
    public CartesianPoint projectOnto(Line3 line) {
        Preconditions.requireNonNull(line, "line");
        Vector3 offset = subtract(line.origin());
        double projection = offset.dot(line.direction().asVector());
        return line.pointAt(projection);
    }

    /**
     * Projects this point onto a plane.
     *
     * @param plane target plane
     * @return projected point on the plane
     */
    public CartesianPoint projectOnto(Plane plane) {
        Preconditions.requireNonNull(plane, "plane");
        double distance = plane.signedDistanceTo(this);
        Vector3 offset = plane.normal().asVector().scale(distance);
        return new CartesianPoint(x - offset.x(), y - offset.y(), z - offset.z());
    }

    /**
     * Returns the square of the distance to another point.
     * More efficient than distanceTo when only comparing distances.
     *
     * @param other target point
     * @return squared distance
     */
    public double distanceSquaredTo(CartesianPoint other) {
        Preconditions.requireNonNull(other, "other");
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Returns a point displaced by a given distance in a direction.
     *
     * @param direction direction to move
     * @param distance distance to move
     * @return displaced point
     */
    public CartesianPoint offset(Direction3 direction, double distance) {
        Preconditions.requireNonNull(direction, "direction");
        Preconditions.requireFinite(distance, "distance");
        Vector3 offset = direction.asVector().scale(distance);
        return add(offset);
    }

    /**
     * Returns a point displaced by a given vector.
     *
     * @param displacement displacement vector
     * @return displaced point
     */
    public CartesianPoint offset(Vector3 displacement) {
        Preconditions.requireNonNull(displacement, "displacement");
        return add(displacement);
    }

    /**
     * Checks if this point is approximately equal to another point.
     *
     * @param other other point
     * @param tolerance tolerance for comparison
     * @return true if points are within tolerance
     */
    public boolean approxEquals(CartesianPoint other, double tolerance) {
        Preconditions.requireNonNull(other, "other");
        Preconditions.requireFinite(tolerance, "tolerance");
        return distanceTo(other) <= tolerance;
    }

    /**
     * Returns the point scaled uniformly about an origin.
     *
     * @param origin origin point for scaling
     * @param factor scale factor
     * @return scaled point
     */
    public CartesianPoint scaleAbout(CartesianPoint origin, double factor) {
        Preconditions.requireNonNull(origin, "origin");
        Preconditions.requireFinite(factor, "factor");
        Vector3 offset = subtract(origin);
        return origin.add(offset.scale(factor));
    }

    /**
     * Returns the point mirrored through a plane.
     *
     * @param plane mirror plane
     * @return mirrored point
     */
    public CartesianPoint mirrorThrough(Plane plane) {
        Preconditions.requireNonNull(plane, "plane");
        double signedDistance = plane.signedDistanceTo(this);
        Vector3 mirrorOffset = plane.normal().asVector().scale(2.0 * signedDistance);
        return subtractVector(mirrorOffset);
    }

    /**
     * Returns the point transformed by a transformation matrix.
     *
     * @param transformation transformation to apply
     * @return transformed point
     */
    public CartesianPoint transform(Transformation3 transformation) {
        Preconditions.requireNonNull(transformation, "transformation");
        return transformation.transform(this);
    }

    /**
     * Returns a 2D point by dropping the z coordinate.
     *
     * @return 2D point with x and y coordinates
     */
    public com.minicad.geometry2d.Point2 toPoint2() {
        return new com.minicad.geometry2d.Point2(x, y);
    }

    /**
     * Returns a point at the origin (0, 0, 0).
     *
     * @return origin point
     */
    public static CartesianPoint origin() {
        return new CartesianPoint(0, 0, 0);
    }

    /**
     * Creates a point from an array of coordinates.
     *
     * @param coords coordinate array (must have exactly 3 elements)
     * @return point from coordinates
     */
    public static CartesianPoint fromArray(double[] coords) {
        Preconditions.requireNonNull(coords, "coords");
        if (coords.length != 3) {
            throw new IllegalArgumentException("coordinate array must have exactly 3 elements");
        }
        return new CartesianPoint(coords[0], coords[1], coords[2]);
    }
}
