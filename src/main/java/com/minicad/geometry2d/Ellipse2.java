package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 2D ellipse representation.
 *
 * @param center ellipse center
 * @param xDirection local x direction
 * @param semiAxis1 local x semi-axis
 * @param semiAxis2 local y semi-axis
 */
public record Ellipse2(Point2 center, Direction2 xDirection, double semiAxis1, double semiAxis2) implements Curve2 {

    public Ellipse2 {
        Preconditions.requireNonNull(center, "center");
        Preconditions.requireNonNull(xDirection, "xDirection");
        Preconditions.requireFinite(semiAxis1, "semiAxis1");
        Preconditions.requireFinite(semiAxis2, "semiAxis2");
        if (semiAxis1 <= Epsilon.EPS || semiAxis2 <= Epsilon.EPS) {
            throw new GeometryException("ellipse semi axes must be greater than epsilon");
        }
    }

    public Point2 pointAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        return center.add(x.scale(Math.cos(angle) * semiAxis1).add(y.scale(Math.sin(angle) * semiAxis2)));
    }

    public double angleOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (!contains(point)) {
            throw new GeometryException("point must lie on the ellipse");
        }
        Vector2 offset = point.subtract(center);
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double normalizedX = offset.dot(x) / semiAxis1;
        double normalizedY = offset.dot(y) / semiAxis2;
        double angle = Math.atan2(normalizedY, normalizedX);
        return angle >= 0.0 ? angle : angle + Math.PI * 2.0;
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(center);
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double normalizedX = offset.dot(x) / semiAxis1;
        double normalizedY = offset.dot(y) / semiAxis2;
        return Epsilon.equals((normalizedX * normalizedX) + (normalizedY * normalizedY), 1.0);
    }

    /**
     * Samples the ellipse at discrete points.
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
     * Samples an arc of the ellipse at discrete points.
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
     * Computes the tangent vector at a given angle on the ellipse.
     *
     * @param angle angle in radians
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        // Derivative: -sin(angle) * semiAxis1 * xDir + cos(angle) * semiAxis2 * yDir
        Vector2 tangent = x.scale(-Math.sin(angle) * semiAxis1).add(y.scale(Math.cos(angle) * semiAxis2));
        if (tangent.norm() <= Epsilon.EPS) {
            return y;
        }
        return tangent.normalize().asVector();
    }

    /**
     * Computes the normal vector at a given angle on the ellipse.
     * The normal points outward from the center.
     *
     * @param angle angle in radians
     * @return unit normal vector pointing outward
     */
    public Vector2 normalAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        // For ellipse, outward normal proportional to (cos/semiAxis1^2, sin/semiAxis2^2)
        double nx = Math.cos(angle) / (semiAxis1 * semiAxis1);
        double ny = Math.sin(angle) / (semiAxis2 * semiAxis2);
        Vector2 normal = x.scale(nx).add(y.scale(ny));
        if (normal.norm() <= Epsilon.EPS) {
            return x;
        }
        return normal.normalize().asVector();
    }

    /**
     * Computes the curvature at a given angle on the ellipse.
     * Curvature formula: k = (a*b) / (a^2*sin^2 + b^2*cos^2)^(3/2)
     *
     * @param angle angle in radians
     * @return curvature
     */
    public double curvatureAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        double a = semiAxis1;
        double b = semiAxis2;
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        double denominator = Math.pow(a * a * sinAngle * sinAngle + b * b * cosAngle * cosAngle, 1.5);
        return (a * b) / denominator;
    }

    /**
     * Approximates the perimeter of the ellipse using Ramanujan's formula.
     *
     * @return approximate perimeter
     */
    @Override
    public double length() {
        return perimeter();
    }

    /**
     * Approximates the perimeter of the ellipse using Ramanujan's formula.
     *
     * @return approximate perimeter
     */
    public double perimeter() {
        double a = semiAxis1;
        double b = semiAxis2;
        double h = Math.pow((a - b) / (a + b), 2);
        return Math.PI * (a + b) * (1.0 + 3.0 * h / (10.0 + Math.sqrt(4.0 - 3.0 * h)));
    }

    /**
     * Returns the area of the ellipse.
     *
     * @return area (PI * semiAxis1 * semiAxis2)
     */
    public double area() {
        return Math.PI * semiAxis1 * semiAxis2;
    }

    /**
     * Returns the bounding box of the full ellipse.
     *
     * @return bounding box enclosing the ellipse
     */
    public BoundingBox2 boundingBox() {
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double extentX = semiAxis1 * Math.abs(x.x()) + semiAxis2 * Math.abs(y.x());
        double extentY = semiAxis1 * Math.abs(x.y()) + semiAxis2 * Math.abs(y.y());
        return new BoundingBox2(
            center.x() - extentX,
            center.y() - extentY,
            center.x() + extentX,
            center.y() + extentY
        );
    }

    /**
     * Returns the closest point on the ellipse to a given point.
     *
     * @param point target point
     * @return approximate closest point on the ellipse
     */
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(center);
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double px = offset.dot(x) / semiAxis1;
        double py = offset.dot(y) / semiAxis2;
        double angle = Math.atan2(py, px);
        return pointAt(angle);
    }

    /**
     * Returns the distance from a point to the ellipse.
     *
     * @param point target point
     * @return approximate distance to the ellipse
     */
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the semi-major axis (larger of the two).
     *
     * @return semi-major axis
     */
    public double semiMajorAxis() {
        return Math.max(semiAxis1, semiAxis2);
    }

    /**
     * Returns the semi-minor axis (smaller of the two).
     *
     * @return semi-minor axis
     */
    public double semiMinorAxis() {
        return Math.min(semiAxis1, semiAxis2);
    }

    /**
     * Returns the eccentricity of the ellipse.
     *
     * @return eccentricity (0 to 1)
     */
    public double eccentricity() {
        double a = semiMajorAxis();
        double b = semiMinorAxis();
        return Math.sqrt(1.0 - b * b / (a * a));
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
     * Returns the focus points of the ellipse.
     * For a circle (semiAxis1 == semiAxis2), both foci are at the center.
     *
     * @return array of two focus points
     */
    public Point2[] foci() {
        double a = semiMajorAxis();
        double b = semiMinorAxis();
        double c = Math.sqrt(a * a - b * b);
        Direction2 majorDir = semiAxis1 >= semiAxis2 ? xDirection : yDirection();
        Point2 f1 = center.add(majorDir.asVector().scale(-c));
        Point2 f2 = center.add(majorDir.asVector().scale(c));
        return new Point2[]{f1, f2};
    }

    /**
     * Creates an ellipse at a center point with standard orientation.
     *
     * @param center center point
     * @param semiAxis1 first semi-axis
     * @param semiAxis2 second semi-axis
     * @return ellipse with x-axis orientation
     */
    public static Ellipse2 at(Point2 center, double semiAxis1, double semiAxis2) {
        return new Ellipse2(center, new Direction2(1, 0), semiAxis1, semiAxis2);
    }
}
