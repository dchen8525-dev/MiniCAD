package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 2D circle representation.
 *
 * @param center circle center
 * @param xDirection local x direction
 * @param radius positive radius
 */
public record Circle2(Point2 center, Direction2 xDirection, double radius) implements Curve2 {

    public Circle2 {
        Preconditions.requireNonNull(center, "center");
        Preconditions.requireNonNull(xDirection, "xDirection");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("radius must be greater than epsilon");
        }
    }

    public Point2 pointAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        return center.add(x.scale(Math.cos(angle) * radius).add(y.scale(Math.sin(angle) * radius)));
    }

    public double angleOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (!contains(point)) {
            throw new GeometryException("point must lie on the circle");
        }
        Vector2 offset = point.subtract(center);
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double angle = Math.atan2(offset.dot(y), offset.dot(x));
        return angle >= 0.0 ? angle : angle + Math.PI * 2.0;
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return Math.abs(point.subtract(center).norm() - radius) <= Epsilon.EPS;
    }

    /**
     * Samples the circle at discrete points.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    public java.util.List<Point2> sample(int segments) {
        java.util.List<Point2> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            points.add(pointAt(angle));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Samples an arc of the circle at discrete points.
     *
     * @param segments number of segments
     * @param startAngle start angle in radians
     * @param endAngle end angle in radians
     * @return list of sampled points
     */
    public java.util.List<Point2> sample(int segments, double startAngle, double endAngle) {
        java.util.List<Point2> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + (endAngle - startAngle) * i / segments;
            points.add(pointAt(angle));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Computes the tangent vector at a given angle on the circle.
     *
     * @param angle angle in radians
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        // Tangent: -sin(angle) * xDir + cos(angle) * yDir
        Vector2 tangent = x.scale(-Math.sin(angle)).add(y.scale(Math.cos(angle)));
        if (tangent.norm() <= Epsilon.EPS) {
            return y;
        }
        return tangent.normalize().asVector();
    }

    /**
     * Computes the normal vector at a given angle on the circle.
     * The normal points outward from the center.
     *
     * @param angle angle in radians
     * @return unit normal vector pointing outward
     */
    public Vector2 normalAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        // Normal: cos(angle) * xDir + sin(angle) * yDir
        Vector2 normal = x.scale(Math.cos(angle)).add(y.scale(Math.sin(angle)));
        if (normal.norm() <= Epsilon.EPS) {
            return x;
        }
        return normal.normalize().asVector();
    }

    /**
     * Returns the curvature of the circle.
     * For a circle, curvature is constant and equal to 1/radius.
     *
     * @return curvature (1/radius)
     */
    public double curvature() {
        return 1.0 / radius;
    }

    /**
     * Returns the curvature at any point on the circle.
     * For a circle, curvature is constant.
     *
     * @param angle angle in radians (curvature is independent of angle)
     * @return curvature (1/radius)
     */
    public double curvatureAt(double angle) {
        return curvature();
    }

    /**
     * Returns the circumference of the circle.
     *
     * @return circumference (2*PI*radius)
     */
    public double circumference() {
        return 2.0 * Math.PI * radius;
    }

    /**
     * Returns the area of the circle.
     *
     * @return area (PI*radius^2)
     */
    public double area() {
        return Math.PI * radius * radius;
    }

    /**
     * Returns the arc length between two angles.
     *
     * @param startAngle start angle in radians
     * @param endAngle end angle in radians
     * @return arc length
     */
    public double arcLength(double startAngle, double endAngle) {
        Preconditions.requireFinite(startAngle, "startAngle");
        Preconditions.requireFinite(endAngle, "endAngle");
        return Math.abs(endAngle - startAngle) * radius;
    }

    /**
     * Returns the bounding box of the full circle.
     *
     * @return bounding box enclosing the circle
     */
    public BoundingBox2 boundingBox() {
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double extentX = radius * (Math.abs(x.x()) + Math.abs(y.x()));
        double extentY = radius * (Math.abs(x.y()) + Math.abs(y.y()));
        return new BoundingBox2(
            center.x() - extentX,
            center.y() - extentY,
            center.x() + extentX,
            center.y() + extentY
        );
    }

    /**
     * Returns the closest point on the circle to a given point.
     *
     * @param point target point
     * @return closest point on the circle
     */
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(center);
        if (offset.isZero()) {
            return pointAt(0);
        }
        double angle = Math.atan2(offset.dot(new Vector2(-xDirection.y(), xDirection.x())), offset.dot(xDirection.asVector()));
        return pointAt(angle);
    }

    /**
     * Returns the distance from a point to the circle.
     *
     * @param point target point
     * @return shortest distance to the circle
     */
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the diameter of the circle.
     *
     * @return diameter (2*radius)
     */
    public double diameter() {
        return 2.0 * radius;
    }

    /**
     * Returns the center point.
     *
     * @return center point
     */
    public Point2 centerPoint() {
        return center;
    }

    /**
     * Returns the y direction (perpendicular to x direction).
     *
     * @return y direction
     */
    public Direction2 yDirection() {
        return new Direction2(-xDirection.y(), xDirection.x());
    }

    /**
     * Creates a circle at a center point with standard orientation.
     *
     * @param center center point
     * @param radius radius
     * @return circle with x-axis orientation
     */
    public static Circle2 at(Point2 center, double radius) {
        return new Circle2(center, new Direction2(1, 0), radius);
    }
}
