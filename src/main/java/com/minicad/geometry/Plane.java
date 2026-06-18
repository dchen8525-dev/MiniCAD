package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Infinite 3D plane defined by a point and a unit normal.
 *
 * @param origin point on the plane
 * @param normal unit plane normal
 */
/**
 * Infinite 3D plane defined by a point and a unit normal.
 *
 * @param origin point on the plane
 * @param normal unit plane normal
 */
public final class Plane implements SurfaceGeometry {
    private final CartesianPoint origin;
    private final Direction3 normal;

    public Plane(CartesianPoint origin, Direction3 normal) {
        this.origin = origin;
        this.normal = normal;
    }

    public CartesianPoint getOrigin() {
        return origin;
    }

    public Direction3 getNormal() {
        return normal;
    }

    // Record-style accessors
    public CartesianPoint origin() { return getOrigin(); }
    public Direction3 normal() { return getNormal(); }

    /**
     * Computes the signed distance from a point to this plane.
     * Positive distance means the point is on the side where the normal points.
     *
     * @param point the point to measure distance from
     * @return signed distance from point to plane
     */
    public double signedDistanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(origin);
        return offset.dot(normal.asVector());
    }

    /**
     * Computes the absolute distance from a point to this plane.
     *
     * @param point the point to measure distance from
     * @return absolute distance from point to plane
     */
    public double distanceTo(CartesianPoint point) {
        return Math.abs(signedDistanceTo(point));
    }

    /**
     * Checks if a point lies on this plane (within epsilon tolerance).
     *
     * @param point point to check
     * @return true if point is on the plane
     */
    public boolean contains(CartesianPoint point) {
        return distanceTo(point) < Epsilon.get();
    }

    /**
     * Intersects a line with this plane.
     *
     * @param line the line to intersect
     * @return intersection point
     * @throws GeometryException if line is parallel to plane
     */
    public CartesianPoint intersect(Line3 line) {
        Preconditions.requireNonNull(line, "line");
        Vector3 lineDir = line.direction().asVector();
        double denom = normal.dot(lineDir);
        if (Math.abs(denom) < Epsilon.get()) {
            throw new GeometryException("line is parallel to plane");
        }
        Vector3 toLineOrigin = line.origin().subtract(origin);
        double t = -normal.dot(toLineOrigin) / denom;
        return line.pointAt(t);
    }

    /**
     * Returns the normal vector at any point on the plane.
     * The normal is constant for a plane.
     *
     * @return normal vector
     */
    public Vector3 normalAt() {
        return normal.asVector();
    }

    /**
     * Returns the normal vector at a parametric position.
     *
     * @param u parametric coordinate (ignored for plane)
     * @param v parametric coordinate (ignored for plane)
     * @return normal vector
     */
    public Vector3 normalAt(double u, double v) {
        return normal.asVector();
    }

    /**
     * Returns a point on the plane at parametric coordinates.
     *
     * @param u parametric coordinate along local x
     * @param v parametric coordinate along local y
     * @return point on the plane
     */
    public CartesianPoint pointAt(double u, double v) {
        // For a plane, we need two perpendicular directions
        Direction3 xDir = normal.perpendicular();
        Direction3 yDir = normal.cross(xDir);
        return origin.add(xDir.asVector().scale(u)).add(yDir.asVector().scale(v));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane that = (Plane) o;
        return Objects.equals(origin, that.origin) && Objects.equals(normal, that.normal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, normal);
    }

    @Override
    public String toString() {
        return "Plane{" + "origin=" + origin + "normal=" + normal + "}";
    }
}
