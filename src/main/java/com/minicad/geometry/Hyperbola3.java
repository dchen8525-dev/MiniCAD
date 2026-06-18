package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param position hyperbola placement (center at origin, transverse axis along local X)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
/**
 * Minimal 3D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param position hyperbola placement (center at origin, transverse axis along local X)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
public final class Hyperbola3 implements Curve3 {
    private final Axis2Placement3D position;
    private final double semiAxisA;
    private final double semiAxisB;

    public Hyperbola3(Axis2Placement3D position, double semiAxisA, double semiAxisB) {
        this.position = position;
        this.semiAxisA = semiAxisA;
        this.semiAxisB = semiAxisB;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getSemiAxisA() {
        return semiAxisA;
    }

    public double getSemiAxisB() {
        return semiAxisB;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return position; }
    public double semiAxisA() { return semiAxisA; }
    public double semiAxisB() { return semiAxisB; }

    // Convenience aliases
    public double semiMajorAxis() { return semiAxisA; }
    public double semiMinorAxis() { return semiAxisB; }

    /**
     * Returns the eccentricity of the hyperbola.
     * Eccentricity = sqrt(1 + (b/a)^2)
     *
     * @return eccentricity
     */
    public double eccentricity() {
        return Math.sqrt(1.0 + (semiAxisB * semiAxisB) / (semiAxisA * semiAxisA));
    }

    /**
     * Returns the curvature at a parametric position.
     *
     * @param t parametric value
     * @return curvature
     */
    public double curvatureAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Curvature for hyperbola: k = (a * b) / (a^2 * sinh^2(t) + b^2 * cosh^2(t))^(3/2)
        double sinhT = Math.sinh(t);
        double coshT = Math.cosh(t);
        double numerator = semiAxisA * semiAxisB;
        double denominator = Math.pow(
            semiAxisA * semiAxisA * sinhT * sinhT + semiAxisB * semiAxisB * coshT * coshT,
            1.5
        );
        return numerator / denominator;
    }

    /**
     * Samples one branch of the hyperbola.
     *
     * @param branch branch index (1 for positive, -1 for negative)
     * @param segments number of segments to sample
     * @return sampled points on the specified branch
     */
    public java.util.List<CartesianPoint> sampleBranch(int branch, int segments) {
        return sampleBranch(branch, segments, -5.0, 5.0, false);
    }

    /**
     * Samples one branch of the hyperbola with a specified parameter range.
     *
     * @param branch branch index (1 for positive, -1 for negative)
     * @param segments number of segments to sample
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @param includeEndPoints whether to include both endpoints
     * @return sampled points on the specified branch
     */
    public java.util.List<CartesianPoint> sampleBranch(int branch, int segments, double tMin, double tMax, boolean includeEndPoints) {
        Preconditions.requireFinite(tMin, "tMin");
        Preconditions.requireFinite(tMax, "tMax");
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        double sign = branch >= 0 ? 1.0 : -1.0;
        int offset = includeEndPoints ? 0 : 1;
        int endOffset = includeEndPoints ? 0 : -1;
        for (int i = offset; i <= segments + endOffset; i++) {
            double t = tMin + (tMax - tMin) * i / segments;
            double xLocal = semiAxisA * Math.cosh(t);
            double yLocal = sign * semiAxisB * Math.sinh(t);
            CartesianPoint localPoint = new CartesianPoint(xLocal, yLocal, 0);
            points.add(position.transformToWorld(localPoint));
        }
        return java.util.List.copyOf(points);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hyperbola3 that = (Hyperbola3) o;
        return Objects.equals(position, that.position) && semiAxisA == that.semiAxisA && semiAxisB == that.semiAxisB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, semiAxisA, semiAxisB);
    }

    @Override
    public String toString() {
        return "Hyperbola3{" + "position=" + position + "semiAxisA=" + semiAxisA + "semiAxisB=" + semiAxisB + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Hyperbola parametric equation: x = a * cosh(t), y = b * sinh(t)
        double coshT = Math.cosh(parameter);
        double sinhT = Math.sinh(parameter);
        double xLocal = semiAxisA * coshT;
        double yLocal = semiAxisB * sinhT;
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
            double t = 0.5 * i; // Scale to get meaningful range
            points.add(pointAt(t));
        }
        return java.util.List.copyOf(points);
    }
}