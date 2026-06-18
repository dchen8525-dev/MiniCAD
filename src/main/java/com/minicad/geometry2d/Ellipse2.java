package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D ellipse representation.
 *
 * @param center ellipse center
 * @param xDirection local x direction
 * @param semiAxis1 local x semi-axis
 * @param semiAxis2 local y semi-axis
 */
/**
 * Minimal 2D ellipse representation.
 *
 * @param center ellipse center
 * @param xDirection local x direction
 * @param semiAxis1 local x semi-axis
 * @param semiAxis2 local y semi-axis
 */
public final class Ellipse2 implements Curve2 {
    private final Point2 center;
    private final Direction2 xDirection;
    private final double semiAxis1;
    private final double semiAxis2;

    public Ellipse2(Point2 center, Direction2 xDirection, double semiAxis1, double semiAxis2) {
        this.center = center;
        this.xDirection = xDirection;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
    }

    public Point2 getCenter() {
        return center;
    }

    public Direction2 getXDirection() {
        return xDirection;
    }

    public double getSemiAxis1() {
        return semiAxis1;
    }

    public double getSemiAxis2() {
        return semiAxis2;
    }

    // Record-style accessors
    public Point2 center() { return getCenter(); }
    public Direction2 xDirection() { return getXDirection(); }
    public double semiAxis1() { return getSemiAxis1(); }
    public double semiAxis2() { return getSemiAxis2(); }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Parameter is the angle in radians (parametric angle, not geometric angle for general ellipse)
        double cosA = Math.cos(parameter);
        double sinA = Math.sin(parameter);
        Vector2 xDir = xDirection.asVector();
        Vector2 yDir = xDirection.perpendicular().asVector();
        return center.add(xDir.scale(semiAxis1 * cosA)).add(yDir.scale(semiAxis2 * sinA));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ellipse2 that = (Ellipse2) o;
        return Objects.equals(center, that.center) && Objects.equals(xDirection, that.xDirection) && semiAxis1 == that.semiAxis1 && semiAxis2 == that.semiAxis2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, xDirection, semiAxis1, semiAxis2);
    }

    @Override
    public String toString() {
        return "Ellipse2{" + "center=" + center + "xDirection=" + xDirection + "semiAxis1=" + semiAxis1 + "semiAxis2=" + semiAxis2 + "}";
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // Transform point to local coordinates
        Vector2 toPoint = point.subtract(center);
        Vector2 xDir = xDirection.asVector();
        Vector2 yDir = xDirection.perpendicular().asVector();
        double xLocal = toPoint.dot(xDir);
        double yLocal = toPoint.dot(yDir);
        // Check ellipse equation: (x/a)^2 + (y/b)^2 = 1
        double value = (xLocal * xLocal) / (semiAxis1 * semiAxis1) + (yLocal * yLocal) / (semiAxis2 * semiAxis2);
        return Math.abs(value - 1.0) < Epsilon.get();
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
     * Returns the parametric angle corresponding to a point on the ellipse.
     *
     * @param point a point on or near the ellipse
     * @return parametric angle in radians
     */
    public double angleOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 toPoint = point.subtract(center);
        Vector2 xDir = xDirection.asVector();
        Vector2 yDir = xDirection.perpendicular().asVector();
        double xLocal = toPoint.dot(xDir);
        double yLocal = toPoint.dot(yDir);
        // Parametric angle: atan2(y/b, x/a)
        return Math.atan2(yLocal / semiAxis2, xLocal / semiAxis1);
    }

    /**
     * Returns the normal vector at a given parameter.
     *
     * @param parameter parametric angle in radians
     * @return normal vector (perpendicular to tangent)
     */
    public Vector2 normalAt(double parameter) {
        double cosA = Math.cos(parameter);
        double sinA = Math.sin(parameter);
        // Normal points outward from ellipse center
        // For ellipse, normal direction: (cos/a, sin/b) normalized
        double nx = cosA / semiAxis1;
        double ny = sinA / semiAxis2;
        double len = Math.sqrt(nx * nx + ny * ny);
        return new Vector2(nx / len, ny / len);
    }

    /**
     * Returns the normal vector at a given segment index.
     *
     * @param segment segment index
     * @return normal vector
     */
    public Vector2 normalAt(int segment) {
        double angle = 2 * Math.PI * segment / 100;
        return normalAt(angle);
    }

    /**
     * Returns the curvature at a given parameter.
     *
     * @param parameter parametric angle in radians
     * @return curvature value
     */
    public double curvatureAt(double parameter) {
        // Curvature of ellipse: (ab) / ((a^2 sin^2 + b^2 cos^2)^(3/2))
        double cosA = Math.cos(parameter);
        double sinA = Math.sin(parameter);
        double a = semiAxis1;
        double b = semiAxis2;
        double denom = Math.pow(a * a * sinA * sinA + b * b * cosA * cosA, 1.5);
        return (a * b) / denom;
    }

    /**
     * Returns the perimeter (approximate).
     * Uses Ramanujan's approximation.
     *
     * @return approximate perimeter
     */
    public double perimeter() {
        double a = semiAxis1;
        double b = semiAxis2;
        // Ramanujan's approximation: PI * (3(a+b) - sqrt((3a+b)(a+3b)))
        return Math.PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
    }

    /**
     * Returns the area of the ellipse.
     *
     * @return area (PI * a * b)
     */
    public double area() {
        return Math.PI * semiAxis1 * semiAxis2;
    }

    /**
     * Returns the semi-major axis (the larger of the two semi-axes).
     *
     * @return semi-major axis length
     */
    public double semiMajorAxis() {
        return Math.max(semiAxis1, semiAxis2);
    }

    /**
     * Returns the semi-minor axis (the smaller of the two semi-axes).
     *
     * @return semi-minor axis length
     */
    public double semiMinorAxis() {
        return Math.min(semiAxis1, semiAxis2);
    }

    /**
     * Returns the eccentricity of the ellipse.
     *
     * @return eccentricity (0 for circle, approaches 1 for very flat ellipse)
     */
    public double eccentricity() {
        double a = semiMajorAxis();
        double b = semiMinorAxis();
        return Math.sqrt(1 - (b * b) / (a * a));
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
     * Returns the two foci of the ellipse.
     *
     * @return list of two focus points
     */
    public List<Point2> foci() {
        double a = semiMajorAxis();
        double b = semiMinorAxis();
        double c = Math.sqrt(a * a - b * b);  // distance from center to focus
        Direction2 majorDir = (semiAxis1 >= semiAxis2) ? xDirection : xDirection.perpendicular();
        return List.of(
            center.add(majorDir.asVector().scale(c)),
            center.subtract(majorDir.asVector().scale(c))
        );
    }

    /**
     * Creates an ellipse at a given position with given orientation.
     *
     * @param position center position
     * @param semiAxis1 first semi-axis
     * @param semiAxis2 second semi-axis
     * @return new ellipse
     */
    public static Ellipse2 at(Point2 position, double semiAxis1, double semiAxis2) {
        return new Ellipse2(position, Direction2.xAxis(), semiAxis1, semiAxis2);
    }
}
