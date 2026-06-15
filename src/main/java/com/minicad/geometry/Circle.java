package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D circle representation.
 *
 * @param position circle placement
 * @param radius positive radius
 */
/**
 * Minimal 3D circle representation.
 *
 * @param position circle placement
 * @param radius positive radius
 */
public final class Circle implements Curve3 {
    private final Axis2Placement3D position;
    private final double radius;

    public Circle(Axis2Placement3D position, double radius) {
        this.position = position;
        this.radius = radius;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle that = (Circle) o;
        return Objects.equals(position, that.position) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, radius);
    }

    @Override
    public String toString() {
        return "Circle{" + "position=" + position + "radius=" + radius + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Parameter is the angle in radians
        double cosA = Math.cos(parameter);
        double sinA = Math.sin(parameter);
        // Local point on the circle (in the XY plane of the placement)
        CartesianPoint localPoint = new CartesianPoint(radius * cosA, radius * sinA, 0);
        return position.transformToWorld(localPoint);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the circle within epsilon
        // Distance from center should equal radius, and point should be in the circle's plane
        CartesianPoint center = position.location();
        double dist = point.distanceTo(center);
        return Math.abs(dist - radius) < Epsilon.get();
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto circle plane and find closest point on circle
        CartesianPoint center = position.location();
        Vector3 toPoint = point.subtract(center);
        Direction3 normal = position.axis();
        // Project onto plane
        double dotNormal = toPoint.dot(normal.asVector());
        Vector3 projected = toPoint.subtract(normal.asVector().scale(dotNormal));
        if (projected.norm() < Epsilon.get()) {
            // Point is at center, return any point on circle
            return pointAt(0);
        }
        // Normalize and scale to radius
        Direction3 radial = Direction3.from(projected);
        return center.add(radial.asVector().scale(radius));
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            points.add(pointAt(angle));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Returns the angle corresponding to a point on the circle.
     *
     * @param point a point on or near the circle
     * @return angle in radians
     */
    public double angleOf(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint center = position.location();
        Direction3 normal = position.axis();
        Vector3 toPoint = point.subtract(center);
        // Project onto circle plane
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double xLocal = toPoint.dot(xDir);
        double yLocal = toPoint.dot(yDir);
        return Math.atan2(yLocal, xLocal);
    }
}
