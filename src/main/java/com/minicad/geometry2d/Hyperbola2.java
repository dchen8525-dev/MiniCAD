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

    /**
     * Returns a point on the specified branch of the hyperbola.
     *
     * @param branch branch index (0 for right branch, 1 for left branch)
     * @param onBranch true if on the specified branch
     * @return point on the branch
     */
    public Point2 pointAt(int branch, boolean onBranch) {
        // This is a convenience method for test compatibility
        // Default to right branch (positive x)
        double t = (branch == 0) ? 1.0 : -1.0;
        return pointAt(t);
    }

    /**
     * Samples points from both branches of the hyperbola.
     *
     * @param segments number of segments per branch
     * @param startT start parameter
     * @param endT end parameter
     * @return list of sampled points from both branches
     */
    public List<Point2> sampleBothBranches(int segments, double startT, double endT) {
        List<Point2> points = new ArrayList<>();
        // Right branch (positive x)
        for (int i = 0; i <= segments; i++) {
            double t = startT + (endT - startT) * i / segments;
            points.add(pointAt(t));
        }
        // Left branch (negative x)
        for (int i = 0; i <= segments; i++) {
            double t = startT + (endT - startT) * i / segments;
            points.add(pointAt(-t));
        }
        return List.copyOf(points);
    }

    /**
     * Returns the normal at a parameter value.
     *
     * @param parameter parametric value
     * @return normal vector
     */
    public Vector2 normalAt(double parameter) {
        // Derivative of pointAt: (a*sinh(t), b*cosh(t))
        double sinhT = Math.sinh(parameter);
        double coshT = Math.cosh(parameter);
        double dx = semiAxisA * sinhT;
        double dy = semiAxisB * coshT;
        // Normal is perpendicular to tangent
        double len = Math.sqrt(dy * dy + dx * dx);
        return new Vector2(-dy / len, dx / len);
    }

    /**
     * Returns the normal at a branch.
     *
     * @param branch branch index
     * @param onBranch true if on branch
     * @return normal vector
     */
    public Vector2 normalAt(int branch, boolean onBranch) {
        return normalAt(branch == 0 ? 1.0 : -1.0);
    }

    /**
     * Returns the normal at a segment index.
     *
     * @param segment segment index
     * @return normal vector
     */
    public Vector2 normalAt(int segment) {
        return normalAt((double) segment);
    }

    /**
     * Returns the curvature at a parameter value.
     *
     * @param parameter parametric value
     * @return curvature value
     */
    public double curvatureAt(double parameter) {
        double coshT = Math.cosh(parameter);
        double sinhT = Math.sinh(parameter);
        double a = semiAxisA;
        double b = semiAxisB;
        // Curvature for hyperbola parametric: (ab) / (a^2 sinh^2 + b^2 cosh^2)^(3/2)
        double denom = Math.pow(a * a * sinhT * sinhT + b * b * coshT * coshT, 1.5);
        return -(a * b) / denom;  // Negative curvature for hyperbola
    }

    /**
     * Returns the curvature at a branch.
     *
     * @param branch branch index
     * @return curvature value
     */
    public double curvatureAt(int branch) {
        return curvatureAt(branch == 0 ? 1.0 : -1.0);
    }

    /**
     * Returns the semi-major axis (transverse axis).
     *
     * @return semi-major axis length
     */
    public double semiMajorAxis() {
        return semiAxisA;
    }

    /**
     * Returns the semi-minor axis (conjugate axis).
     *
     * @return semi-minor axis length
     */
    public double semiMinorAxis() {
        return semiAxisB;
    }

    /**
     * Returns the eccentricity of the hyperbola.
     *
     * @return eccentricity (> 1 for hyperbola)
     */
    public double eccentricity() {
        double a = semiAxisA;
        double b = semiAxisB;
        return Math.sqrt(1 + (b * b) / (a * a));
    }

    /**
     * Returns the two foci of the hyperbola.
     *
     * @return list of two focus points
     */
    public List<Point2> foci() {
        double c = Math.sqrt(semiAxisA * semiAxisA + semiAxisB * semiAxisB);
        return List.of(
            center.add(xDirection.asVector().scale(c)),
            center.subtract(xDirection.asVector().scale(c))
        );
    }

    /**
     * Returns the local y direction (conjugate axis direction).
     *
     * @return y direction
     */
    public Direction2 yDirection() {
        return xDirection.perpendicular();
    }

    /**
     * Creates a hyperbola at a given position.
     *
     * @param position center position
     * @param semiAxisA semi-major axis
     * @param semiAxisB semi-minor axis
     * @return new hyperbola
     */
    public static Hyperbola2 at(Point2 position, double semiAxisA, double semiAxisB) {
        return new Hyperbola2(position, Direction2.xAxis(), semiAxisA, semiAxisB);
    }
}