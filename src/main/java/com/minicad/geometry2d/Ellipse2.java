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
}
