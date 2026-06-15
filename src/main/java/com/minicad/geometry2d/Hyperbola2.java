package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param center hyperbola center
 * @param xDirection local x direction (transverse axis direction)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
/**
 * Minimal 2D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param center hyperbola center
 * @param xDirection local x direction (transverse axis direction)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
public final class Hyperbola2 implements Curve2 {
    private final Point2 center;
    private final Direction2 xDirection;
    private final double semiAxisA;
    private final double semiAxisB;

    public Hyperbola2(Point2 center, Direction2 xDirection, double semiAxisA, double semiAxisB) {
        this.center = center;
        this.xDirection = xDirection;
        this.semiAxisA = semiAxisA;
        this.semiAxisB = semiAxisB;
    }

    public Point2 getCenter() {
        return center;
    }

    public Direction2 getXDirection() {
        return xDirection;
    }

    public double getSemiAxisA() {
        return semiAxisA;
    }

    public double getSemiAxisB() {
        return semiAxisB;
    }

    // Record-style accessors
    public Point2 center() { return getCenter(); }
    public Direction2 xDirection() { return getXDirection(); }
    public double semiAxisA() { return getSemiAxisA(); }
    public double semiAxisB() { return getSemiAxisB(); }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Hyperbola parametric equation: x = a * sec(theta), y = b * tan(theta)
        // Using cosh/sinh for better numerical stability: x = a * cosh(t), y = b * sinh(t)
        double coshT = Math.cosh(parameter);
        double sinhT = Math.sinh(parameter);
        double xLocal = semiAxisA * coshT;
        double yLocal = semiAxisB * sinhT;
        Vector2 xDir = xDirection.asVector();
        Vector2 yDir = xDirection.perpendicular().asVector();
        return center.add(xDir.scale(xLocal)).add(yDir.scale(yLocal));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hyperbola2 that = (Hyperbola2) o;
        return Objects.equals(center, that.center) && Objects.equals(xDirection, that.xDirection) && semiAxisA == that.semiAxisA && semiAxisB == that.semiAxisB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, xDirection, semiAxisA, semiAxisB);
    }

    @Override
    public String toString() {
        return "Hyperbola2{" + "center=" + center + "xDirection=" + xDirection + "semiAxisA=" + semiAxisA + "semiAxisB=" + semiAxisB + "}";
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
        // Check hyperbola equation: (x/a)^2 - (y/b)^2 = 1
        double value = (xLocal * xLocal) / (semiAxisA * semiAxisA) - (yLocal * yLocal) / (semiAxisB * semiAxisB);
        return Math.abs(value - 1.0) < Epsilon.get() || Math.abs(value + 1.0) < Epsilon.get();
    }

    @Override
    public List<Point2> sample(int segments) {
        List<Point2> points = new ArrayList<>();
        // Sample a range of parameter values
        for (int i = -segments; i <= segments; i++) {
            double t = 0.5 * i; // Scale to get meaningful range
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }
}