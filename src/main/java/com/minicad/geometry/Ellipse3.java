package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D ellipse representation.
 *
 * @param position ellipse placement
 * @param semiAxis1 semi-axis along local X
 * @param semiAxis2 semi-axis along local Y
 */
/**
 * Minimal 3D ellipse representation.
 *
 * @param position ellipse placement
 * @param semiAxis1 semi-axis along local X
 * @param semiAxis2 semi-axis along local Y
 */
public final class Ellipse3 implements Curve3 {
    private final Axis2Placement3D position;
    private final double semiAxis1;
    private final double semiAxis2;

    public Ellipse3(Axis2Placement3D position, double semiAxis1, double semiAxis2) {
        this.position = position;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getSemiAxis1() {
        return semiAxis1;
    }

    public double getSemiAxis2() {
        return semiAxis2;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return getPosition(); }
    public double semiAxis1() { return getSemiAxis1(); }
    public double semiAxis2() { return getSemiAxis2(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ellipse3 that = (Ellipse3) o;
        return Objects.equals(position, that.position) && semiAxis1 == that.semiAxis1 && semiAxis2 == that.semiAxis2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, semiAxis1, semiAxis2);
    }

    @Override
    public String toString() {
        return "Ellipse3{" + "position=" + position + "semiAxis1=" + semiAxis1 + "semiAxis2=" + semiAxis2 + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Parameter is the angle in radians (parametric angle)
        double cosA = Math.cos(parameter);
        double sinA = Math.sin(parameter);
        // Local point on the ellipse (in the XY plane of the placement)
        CartesianPoint localPoint = new CartesianPoint(semiAxis1 * cosA, semiAxis2 * sinA, 0);
        return position.transformToWorld(localPoint);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the ellipse within epsilon
        // Transform to local coordinates and check ellipse equation
        java.util.List<CartesianPoint> samples = sample(64);
        for (CartesianPoint sample : samples) {
            if (point.distanceTo(sample) < Epsilon.get()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Find closest by sampling
        java.util.List<CartesianPoint> samples = sample(256);
        CartesianPoint closest = samples.get(0);
        double minDist = point.distanceTo(closest);
        for (int i = 1; i < samples.size(); i++) {
            double dist = point.distanceTo(samples.get(i));
            if (dist < minDist) {
                minDist = dist;
                closest = samples.get(i);
            }
        }
        return closest;
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
     * Returns the parametric angle corresponding to a point on the ellipse.
     *
     * @param point a point on or near the ellipse
     * @return parametric angle in radians
     */
    public double angleOf(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint center = position.location();
        Vector3 toPoint = point.subtract(center);
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double xLocal = toPoint.dot(xDir);
        double yLocal = toPoint.dot(yDir);
        // Parametric angle: atan2(y/b, x/a)
        return Math.atan2(yLocal / semiAxis2, xLocal / semiAxis1);
    }
}
