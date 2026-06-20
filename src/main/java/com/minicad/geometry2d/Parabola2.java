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
        Preconditions.requireNonNull(vertex, "vertex");
        Preconditions.requireNonNull(axisDirection, "axisDirection");
        Preconditions.requireFinite(focalDistance, "focalDistance");
        if (focalDistance <= Epsilon.get()) {
            throw new GeometryException("focalDistance must be greater than epsilon");
        }
        this.vertex = vertex;
        this.axisDirection = axisDirection;
        this.focalDistance = focalDistance;
    }

    /**
     * Creates a parabola with default axis direction (Y-axis).
     *
     * @param vertex parabola vertex
     * @param focalDistance distance from vertex to focus
     * @return parabola at the given vertex with default axis direction
     */
    public static Parabola2 at(Point2 vertex, double focalDistance) {
        Preconditions.requireNonNull(vertex, "vertex");
        return new Parabola2(vertex, Direction2.yAxis(), focalDistance);
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
        double xLocal = 2.0 * focalDistance * parameter;
        double yLocal = focalDistance * parameter * parameter;
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
        for (int i = 0; i <= segments; i++) {
            double t = -1.0 + 2.0 * i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }

    /**
     * Samples a portion of the parabola with given parameter bounds.
     *
     * @param segments number of segments
     * @param startParam start parameter
     * @param endParam end parameter
     * @return sampled points
     */
    public List<Point2> sample(int segments, double startParam, double endParam) {
        Preconditions.requireFinite(startParam, "startParam");
        Preconditions.requireFinite(endParam, "endParam");
        List<Point2> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            double t = startParam + (endParam - startParam) * i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }

    /**
     * Returns the normal vector at a parametric position.
     *
     * @param t parametric value
     * @return normal vector
     */
    public Vector2 normalAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Tangent derivative: dx/dt = 1, dy/dt = 2t / (4*f) = t / (2*f)
        // Normal is perpendicular to tangent
        double dx = 2.0 * focalDistance;
        double dy = 2.0 * focalDistance * t;
        // Perpendicular to tangent
        Vector2 tangentLocal = new Vector2(dx, dy);
        Vector2 normalLocal = tangentLocal.perpendicular().normalize();
        // Transform to global coordinates
        Vector2 axis = axisDirection.asVector();
        Vector2 perp = axisDirection.perpendicular().asVector();
        return new Vector2(
            normalLocal.getX() * perp.getX() + normalLocal.getY() * axis.getX(),
            normalLocal.getX() * perp.getY() + normalLocal.getY() * axis.getY()
        );
    }

    /**
     * Returns the normal vector at a segment index.
     *
     * @param segment segment index
     * @return normal vector
     */
    public Vector2 normalAt(int segment) {
        // Map segment index to parameter
        double t = 0.5 * segment;
        return normalAt(t);
    }

    /**
     * Returns the curvature at a parametric position.
     *
     * @param t parametric value
     * @return curvature
     */
    public double curvatureAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Curvature formula for parabola: k = |2 * focalDistance| / ( (1 + (t/(2*f))^2 )^(3/2) )
        return 1.0 / (2.0 * focalDistance * Math.pow(1.0 + t * t, 1.5));
    }

    /**
     * Returns the curvature at a segment index.
     *
     * @param segment segment index
     * @return curvature
     */
    public double curvatureAt(int segment) {
        double t = 0.5 * segment;
        return curvatureAt(t);
    }

    /**
     * Returns the focus point.
     *
     * @return focus point
     */
    public Point2 focus() {
        Vector2 axis = axisDirection.asVector();
        return vertex.add(axis.scale(focalDistance));
    }

    /**
     * Returns the directrix line (line at distance -focalDistance from vertex).
     *
     * @return directrix line
     */
    public Line2 directrix() {
        Vector2 axis = axisDirection.asVector();
        Point2 directrixPoint = vertex.subtract(axis.scale(focalDistance));
        Direction2 perp = axisDirection.perpendicular();
        return new Line2(directrixPoint, perp);
    }

    /**
     * Returns the focal length (same as focalDistance).
     *
     * @return focal length
     */
    public double focalLength() {
        return focalDistance;
    }

    /**
     * Returns the direction perpendicular to the axis (y-direction).
     *
     * @return y-direction
     */
    public Direction2 yDirection() {
        return axisDirection.perpendicular();
    }

    /**
     * Creates a new parabola at a given vertex with a given focal distance.
     *
     * @param newVertex new vertex point
     * @param newFocalDistance new focal distance
     * @return new parabola with same axis direction
     */
    public Parabola2 withVertex(Point2 newVertex, double newFocalDistance) {
        Preconditions.requireNonNull(newVertex, "newVertex");
        Preconditions.requireFinite(newFocalDistance, "newFocalDistance");
        return new Parabola2(newVertex, axisDirection, newFocalDistance);
    }
}
