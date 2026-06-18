package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D clothoid (Euler spiral / transition curve) representation.
 * The clothoid is defined by its curvature varying linearly with arc length.
 *
 * @param position clothoid placement (start point and local coordinate system)
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter (rate of curvature change per unit length)
 */
/**
 * Minimal 3D clothoid (Euler spiral / transition curve) representation.
 * The clothoid is defined by its curvature varying linearly with arc length.
 *
 * @param position clothoid placement (start point and local coordinate system)
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter (rate of curvature change per unit length)
 */
public final class Clothoid3 implements Curve3 {
    private final Axis2Placement3D position;
    private final double xAxisIntercept;
    private final double curvature;

    public Clothoid3(Axis2Placement3D position, double xAxisIntercept, double curvature) {
        this.position = position;
        this.xAxisIntercept = xAxisIntercept;
        this.curvature = curvature;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getXAxisIntercept() {
        return xAxisIntercept;
    }

    public double getCurvature() {
        return curvature;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return position; }
    public double xAxisIntercept() { return xAxisIntercept; }
    public double curvature() { return curvature; }

    // Alias methods for convenience
    public double intercept() { return xAxisIntercept; }
    public double curvatureRate() { return curvature; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clothoid3 that = (Clothoid3) o;
        return Objects.equals(position, that.position) && xAxisIntercept == that.xAxisIntercept && curvature == that.curvature;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, xAxisIntercept, curvature);
    }

    @Override
    public String toString() {
        return "Clothoid3{" + "position=" + position + "xAxisIntercept=" + xAxisIntercept + "curvature=" + curvature + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Clothoid (Euler spiral) parametric equations using Fresnel integrals:
        // x(t) = integral of cos(t^2/2) dt
        // y(t) = integral of sin(t^2/2) dt
        // Simplified approximation for now
        double t = parameter;
        // Approximate Fresnel integrals using simple polynomial approximation
        double xLocal = xAxisIntercept * (1 - curvature * t * t / 6);
        double yLocal = curvature * t * t * t / 6;
        CartesianPoint localPoint = new CartesianPoint(xLocal, yLocal, 0);
        return position.transformToWorld(localPoint);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
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
        // Sample a range of parameter values
        for (int i = -segments; i <= segments; i++) {
            double t = 0.1 * i; // Scale to get meaningful range
            points.add(pointAt(t));
        }
        return java.util.List.copyOf(points);
    }
}