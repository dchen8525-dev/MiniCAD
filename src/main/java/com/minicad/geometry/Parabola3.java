package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D parabola representation.
 * A parabola is a conic section defined by a focus and directrix, or parametrically.
 *
 * @param position parabola placement (vertex at origin, axis along local Y)
 * @param focalDistance distance from vertex to focus
 */
/**
 * Minimal 3D parabola representation.
 * A parabola is a conic section defined by a focus and directrix, or parametrically.
 *
 * @param position parabola placement (vertex at origin, axis along local Y)
 * @param focalDistance distance from vertex to focus
 */
public final class Parabola3 implements Curve3 {
    private final Axis2Placement3D position;
    private final double focalDistance;

    public Parabola3(Axis2Placement3D position, double focalDistance) {
        this.position = position;
        this.focalDistance = focalDistance;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getFocalDistance() {
        return focalDistance;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return position; }
    public double focalLength() { return focalDistance; }
    public double focalDistance() { return focalDistance; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parabola3 that = (Parabola3) o;
        return Objects.equals(position, that.position) && focalDistance == that.focalDistance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, focalDistance);
    }

    @Override
    public String toString() {
        return "Parabola3{" + "position=" + position + "focalDistance=" + focalDistance + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Parabola parametric equation: x = t, y = t^2 / (4 * f)
        // In local coordinates: x = parameter, y = parameter^2 / (4 * focalDistance), z = 0
        double xLocal = parameter;
        double yLocal = (parameter * parameter) / (4 * focalDistance);
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