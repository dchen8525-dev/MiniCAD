package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D parabola representation.
 * A parabola is a conic section defined by a focus and directrix.
 *
 * @param vertex parabola vertex
 * @param axisDirection axis direction (from vertex towards focus)
 * @param focalDistance distance from vertex to focus
 */
/**
 * Minimal 2D parabola representation.
 * A parabola is a conic section defined by a focus and directrix.
 *
 * @param vertex parabola vertex
 * @param axisDirection axis direction (from vertex towards focus)
 * @param focalDistance distance from vertex to focus
 */
public final class Parabola2 implements Curve2 {
    private final Point2 vertex;
    private final Direction2 axisDirection;
    private final double focalDistance;

    public Parabola2(Point2 vertex, Direction2 axisDirection, double focalDistance) {
        this.vertex = vertex;
        this.axisDirection = axisDirection;
        this.focalDistance = focalDistance;
    }

    public Point2 getVertex() {
        return vertex;
    }

    public Direction2 getAxisDirection() {
        return axisDirection;
    }

    public double getFocalDistance() {
        return focalDistance;
    }

    // Record-style accessors
    public Point2 vertex() { return getVertex(); }
    public Direction2 axisDirection() { return getAxisDirection(); }
    public double focalDistance() { return getFocalDistance(); }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Parabola parametric equation: y = (x^2) / (4 * focalDistance)
        // In local coordinates: x = parameter, y = parameter^2 / (4 * focalDistance)
        double xLocal = parameter;
        double yLocal = (parameter * parameter) / (4 * focalDistance);
        Vector2 axis = axisDirection.asVector();
        Direction2 perpDir = axisDirection.perpendicular();
        Vector2 perp = perpDir.asVector();
        return vertex.add(axis.scale(yLocal)).add(perp.scale(xLocal));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parabola2 that = (Parabola2) o;
        return Objects.equals(vertex, that.vertex) && Objects.equals(axisDirection, that.axisDirection) && focalDistance == that.focalDistance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex, axisDirection, focalDistance);
    }

    @Override
    public String toString() {
        return "Parabola2{" + "vertex=" + vertex + "axisDirection=" + axisDirection + "focalDistance=" + focalDistance + "}";
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // Transform point to local coordinates
        Vector2 toPoint = point.subtract(vertex);
        Vector2 axis = axisDirection.asVector();
        Vector2 perp = axisDirection.perpendicular().asVector();
        double yLocal = toPoint.dot(axis); // Along axis (away from vertex)
        double xLocal = toPoint.dot(perp);  // Perpendicular to axis
        // Check parabola equation: y = x^2 / (4 * focalDistance)
        double expectedY = (xLocal * xLocal) / (4 * focalDistance);
        return Math.abs(yLocal - expectedY) < Epsilon.get();
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