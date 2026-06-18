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

    /**
     * Returns the normal vector at a given parameter (angle).
     *
     * @param parameter angle in radians
     * @return outward normal vector
     */
    public Vector2 normalAt(double parameter) {
        // For a circle, normal points radially outward
        double cosA = Math.cos(parameter);
        double sinA = Math.sin(parameter);
        return new Vector2(cosA, sinA);
    }

    /**
     * Returns the normal vector at a given segment index.
     *
     * @param segment segment index
     * @return outward normal vector
     */
    public Vector2 normalAt(int segment) {
        // Convert segment index to angle
        double angle = 2 * Math.PI * segment / 100; // Default to 100 segments
        return normalAt(angle);
    }

    /**
     * Returns the curvature (constant for a circle = 1/radius).
     *
     * @return curvature value
     */
    public double curvature() {
        return 1.0 / radius;
    }

    /**
     * Returns the curvature at a given parameter (constant for circle).
     *
     * @param parameter angle in radians (ignored for circle)
     * @return curvature value (1/radius)
     */
    public double curvatureAt(double parameter) {
        return curvature();
    }

    /**
     * Returns the circumference (perimeter) of the circle.
     *
     * @return circumference (2 * PI * radius)
     */
    public double circumference() {
        return 2 * Math.PI * radius;
    }

    /**
     * Returns the area of the circle.
     *
     * @return area (PI * radius^2)
     */
    public double area() {
        return Math.PI * radius * radius;
    }

    /**
     * Returns the arc length between two parameters.
     *
     * @param startParam start angle in radians
     * @param endParam end angle in radians
     * @return arc length
     */
    public double arcLength(double startParam, double endParam) {
        double delta = Math.abs(endParam - startParam);
        // Normalize to [0, 2PI]
        while (delta > 2 * Math.PI) delta -= 2 * Math.PI;
        return radius * delta;
    }

    /**
     * Returns the diameter of the circle.
     *
     * @return diameter (2 * radius)
     */
    public double diameter() {
        return 2 * radius;
    }

    /**
     * Returns the center point.
     * Alias for center() for compatibility.
     *
     * @return center point
     */
    public Point2 centerPoint() {
        return center;
    }

    /**
     * Returns the local y direction (perpendicular to x direction).
     *
     * @return y direction
     */
    public Direction2 yDirection() {
        return xDirection.perpendicular();
    }

    /**
     * Creates a circle at a given position with given radius.
     *
     * @param position center position
     * @param radius circle radius
     * @return new circle
     */
    public static Circle2 at(Point2 position, double radius) {
        return new Circle2(position, Direction2.xAxis(), radius);
    }
}
