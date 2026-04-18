package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal 2D parabola representation.
 * A parabola is a conic section defined by a focus and directrix.
 *
 * @param vertex parabola vertex
 * @param axisDirection axis direction (from vertex towards focus)
 * @param focalDistance distance from vertex to focus
 */
public record Parabola2(Point2 vertex, Direction2 axisDirection, double focalDistance) implements Curve2 {

    /**
     * Creates a parabola and validates its invariants.
     */
    public Parabola2 {
        Preconditions.requireNonNull(vertex, "vertex");
        Preconditions.requireNonNull(axisDirection, "axisDirection");
        Preconditions.requireFinite(focalDistance, "focalDistance");
        if (focalDistance <= Epsilon.EPS) {
            throw new GeometryException("parabola focal distance must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the parabola at a given parameter.
     * Parametric form: x = 2*p*t (perpendicular to axis), y = p*t^2 (along axis)
     *
     * @param t parameter value
     * @return point on the parabola
     */
    public Point2 pointAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Standard parabola equation y = x^2 / (4p)
        // Parametric: perpendicular displacement = 2*p*t, axis displacement = p*t^2
        double perpendicularDisplacement = 2.0 * focalDistance * t;
        double axisDisplacement = focalDistance * t * t;

        Vector2 axis = axisDirection.asVector();
        Vector2 perpendicular = new Vector2(-axis.y(), axis.x());
        return vertex.add(axis.scale(axisDisplacement).add(perpendicular.scale(perpendicularDisplacement)));
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(vertex);
        Vector2 axis = axisDirection.asVector();
        Vector2 perpendicular = new Vector2(-axis.y(), axis.x());
        double axisComponent = offset.dot(axis);
        double perpComponent = offset.dot(perpendicular);
        // Parabola equation: axisComponent = perpComponent^2 / (4p)
        return Epsilon.equals(axisComponent, perpComponent * perpComponent / (4.0 * focalDistance));
    }

    /**
     * Samples the parabola at discrete points with parameter range.
     *
     * @param segments number of segments
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @return list of sampled points
     */
    public List<Point2> sample(int segments, double tMin, double tMax) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double t = tMin + (tMax - tMin) * i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }

    /**
     * Samples the parabola from -1 to 1 parameter range.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    public List<Point2> sample(int segments) {
        return sample(segments, -1.0, 1.0);
    }

    /**
     * Computes the tangent vector at a given parameter.
     * Derivative: x = 2*p (constant), y = 2*p*t
     *
     * @param t parameter value
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Derivative: perpendicular = 2*p, axis = 2*p*t
        Vector2 axis = axisDirection.asVector();
        Vector2 perpendicular = new Vector2(-axis.y(), axis.x());
        Vector2 tangent = perpendicular.scale(2.0 * focalDistance).add(axis.scale(2.0 * focalDistance * t));
        if (tangent.norm() <= Epsilon.EPS) {
            return perpendicular;
        }
        return tangent.normalize().asVector();
    }

    /**
     * Computes the normal vector at a given parameter.
     * The normal is perpendicular to the tangent.
     *
     * @param t parameter value
     * @return unit normal vector
     */
    public Vector2 normalAt(double t) {
        Vector2 tangent = tangentAt(t);
        // Rotate tangent 90 degrees
        return new Vector2(-tangent.y(), tangent.x());
    }

    /**
     * Computes the curvature at a given parameter.
     * Curvature of parabola y = x^2/(4p) is: 1/(2p)
     *
     * @param t parameter value
     * @return curvature value
     */
    public double curvatureAt(double t) {
        Preconditions.requireFinite(t, "t");
        // For parabola y = x^2/(4p), curvature = 1/(2p)
        return 1.0 / (2.0 * focalDistance);
    }

    /**
     * Returns the bounding box for a parameter range.
     *
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @return bounding box enclosing the sampled range
     */
    public BoundingBox2 boundingBox(double tMin, double tMax) {
        return BoundingBox2.of(sample(32, tMin, tMax));
    }

    /**
     * Returns the bounding box for default sampling range.
     *
     * @return bounding box enclosing the parabola
     */
    public BoundingBox2 boundingBox() {
        return boundingBox(-1.0, 1.0);
    }

    /**
     * Returns the closest point on the parabola to a given point.
     *
     * @param point target point
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @return closest point on the parabola
     */
    public Point2 closestPointTo(Point2 point, double tMin, double tMax) {
        Preconditions.requireNonNull(point, "point");
        List<Point2> samples = sample(256, tMin, tMax);
        Point2 closest = samples.get(0);
        double minDistance = point.distanceTo(closest);
        for (int i = 1; i < samples.size(); i++) {
            double distance = point.distanceTo(samples.get(i));
            if (distance < minDistance) {
                minDistance = distance;
                closest = samples.get(i);
            }
        }
        return closest;
    }

    /**
     * Returns the closest point on the parabola to a given point.
     *
     * @param point target point
     * @return closest point on the parabola
     */
    public Point2 closestPointTo(Point2 point) {
        return closestPointTo(point, -1.0, 1.0);
    }

    /**
     * Returns the distance from a point to the parabola.
     *
     * @param point target point
     * @return minimum distance to the parabola
     */
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the length of a parabola segment.
     * Closed-form integral: L = p * [t*sqrt(1+t^2) + asinh(t)] from tMin to tMax.
     *
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @return arc length of the segment
     */
    public double length(double tMin, double tMax) {
        Preconditions.requireFinite(tMin, "tMin");
        Preconditions.requireFinite(tMax, "tMax");
        double a = asinhTerm(tMax);
        double b = asinhTerm(tMin);
        return focalDistance * (a - b);
    }

    private static double asinhTerm(double t) {
        return t * Math.sqrt(1.0 + t * t) + Math.log(t + Math.sqrt(1.0 + t * t));
    }

    /**
     * Returns the length of the parabola.
     *
     * @return arc length over the default parameter range [-1, 1]
     */
    @Override
    public double length() {
        return length(-1.0, 1.0);
    }

    /**
     * Returns the focus of the parabola.
     * Focus is located at (0, p) relative to vertex along axis direction.
     *
     * @return focus point
     */
    public Point2 focus() {
        return vertex.add(axisDirection.asVector().scale(focalDistance));
    }

    /**
     * Returns the directrix line of the parabola.
     * Directrix is at y = -p relative to vertex, perpendicular to axis.
     *
     * @return directrix line
     */
    public Line2 directrix() {
        Point2 directrixPoint = vertex.subtract(axisDirection.asVector().scale(focalDistance));
        Vector2 perpendicular = new Vector2(-axisDirection.y(), axisDirection.x());
        return new Line2(directrixPoint, Direction2.from(perpendicular));
    }

    /**
     * Returns the focal length (distance from vertex to focus).
     *
     * @return focal length
     */
    public double focalLength() {
        return focalDistance;
    }

    /**
     * Returns the y direction (perpendicular to axis).
     *
     * @return y direction
     */
    public Direction2 yDirection() {
        Vector2 axis = axisDirection.asVector();
        return Direction2.from(new Vector2(-axis.y(), axis.x()));
    }

    /**
     * Creates a parabola aligned with the y-axis (opening upward).
     *
     * @param vertex parabola vertex
     * @param focalDistance distance from vertex to focus
     * @return parabola opening upward
     */
    public static Parabola2 at(Point2 vertex, double focalDistance) {
        return new Parabola2(vertex, Direction2.from(new Vector2(0, 1)), focalDistance);
    }
}