package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D circle representation.
 *
 * @param center circle center
 * @param xDirection local x direction
 * @param radius positive radius
 */
/**
 * Minimal 2D circle representation.
 *
 * @param center circle center
 * @param xDirection local x direction
 * @param radius positive radius
 */
public final class Circle2 implements Curve2 {
    private final Point2 center;
    private final Direction2 xDirection;
    private final double radius;

    public Circle2(Point2 center, Direction2 xDirection, double radius) {
        this.center = center;
        this.xDirection = xDirection;
        this.radius = radius;
    }

    public Point2 getCenter() {
        return center;
    }

    public Direction2 getXDirection() {
        return xDirection;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessors
    public Point2 center() { return getCenter(); }
    public Direction2 xDirection() { return getXDirection(); }
    public double radius() { return getRadius(); }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Parameter is the angle in radians
        double cosA = Math.cos(parameter);
        double sinA = Math.sin(parameter);
        Vector2 xDir = xDirection.asVector();
        Vector2 yDir = xDirection.perpendicular().asVector();
        return center.add(xDir.scale(radius * cosA)).add(yDir.scale(radius * sinA));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle2 that = (Circle2) o;
        return Objects.equals(center, that.center) && Objects.equals(xDirection, that.xDirection) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, xDirection, radius);
    }

    @Override
    public String toString() {
        return "Circle2{" + "center=" + center + "xDirection=" + xDirection + "radius=" + radius + "}";
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the circle within epsilon
        double distance = point.distanceTo(center);
        return Math.abs(distance - radius) < Epsilon.get();
    }

    @Override
    public List<Point2> sample(int segments) {
        List<Point2> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            points.add(pointAt(angle));
        }
        return List.copyOf(points);
    }

    /**
     * Returns the angle corresponding to a point on the circle.
     *
     * @param point a point on or near the circle
     * @return angle in radians
     */
    public double angleOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 toPoint = point.subtract(center);
        Vector2 xDir = xDirection.asVector();
        Vector2 yDir = xDirection.perpendicular().asVector();
        double xLocal = toPoint.dot(xDir);
        double yLocal = toPoint.dot(yDir);
        return Math.atan2(yLocal, xLocal);
    }
}
