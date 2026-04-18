package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal 3D parabola representation.
 * A parabola is a conic section defined by a focus and directrix, or parametrically.
 *
 * @param position parabola placement (vertex at origin, axis along local Y)
 * @param focalDistance distance from vertex to focus
 */
public record Parabola3(Axis2Placement3D position, double focalDistance) implements Curve3 {

    /**
     * Creates a parabola and validates its invariants.
     */
    public Parabola3 {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(focalDistance, "focalDistance");
        if (focalDistance <= Epsilon.EPS) {
            throw new GeometryException("parabola focal distance must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the parabola at a given parameter.
     * Parametric form: x = 2*p*t, y = p*t^2 where p = focal distance
     *
     * @param t parameter value
     * @return point on the parabola
     */
    @Override
    public CartesianPoint pointAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Standard parabola equation y = x^2 / (4p)
        // Parametric: x = 2*p*t, y = p*t^2
        double x = 2.0 * focalDistance * t;
        double y = focalDistance * t * t;

        // Transform to world coordinates
        Vector3 offset = position.xDirection().asVector().scale(x)
                .add(position.yDirection().asVector().scale(y));
        return position.location().add(offset);
    }

    /**
     * Samples the parabola at discrete points.
     *
     * @param segments number of segments
     * @param tMin minimum parameter value
     * @param tMax maximum parameter value
     * @return list of sampled points
     */
    public List<CartesianPoint> sample(int segments, double tMin, double tMax) {
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
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
    public List<CartesianPoint> sample(int segments) {
        return sample(segments, -1.0, 1.0);
    }

    /**
     * Computes the tangent vector at a given parameter on the parabola.
     *
     * @param t parameter value
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Derivative: dx/dt = 2*p, dy/dt = 2*p*t
        double dx = 2.0 * focalDistance;
        double dy = 2.0 * focalDistance * t;

        Vector3 tangent = position.xDirection().asVector().scale(dx)
                .add(position.yDirection().asVector().scale(dy));
        if (tangent.norm() <= Epsilon.EPS) {
            return position.xDirection().asVector();
        }
        return tangent.normalize().asVector();
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the parabola plane
        Vector3 offset = point.subtract(position.location());
        double planeDistance = Math.abs(offset.dot(position.axis().asVector()));
        if (planeDistance > Epsilon.EPS) {
            return false;
        }
        // Parabola equation in local coordinates: y = x^2 / (4p)
        double x = offset.dot(position.xDirection().asVector());
        double y = offset.dot(position.yDirection().asVector());
        return Epsilon.equals(y, x * x / (4.0 * focalDistance));
    }

    /**
     * Returns the approximate bounding box for a parameter range.
     * Since parabolas are unbounded, this requires parameter bounds.
     *
     * @param tMin minimum parameter
     * @param tMax maximum parameter
     * @return bounding box for the parameter range
     */
    public BoundingBox3 boundingBox(double tMin, double tMax) {
        Preconditions.requireFinite(tMin, "tMin");
        Preconditions.requireFinite(tMax, "tMax");
        BoundingBox3 box = BoundingBox3.empty();
        int segments = Math.max(8, (int) Math.ceil(Math.abs(tMax - tMin) * 4));
        for (int i = 0; i <= segments; i++) {
            double t = tMin + (tMax - tMin) * i / segments;
            box = box.union(pointAt(t));
        }
        return box;
    }

    /**
     * Returns a default bounding box for visualization (t from -1 to 1).
     *
     * @return bounding box for default parameter range
     */
    public BoundingBox3 boundingBox() {
        return boundingBox(-1.0, 1.0);
    }

    /**
     * Computes the curvature at a given parameter on the parabola.
     *
     * @param t parameter value
     * @return curvature
     */
    public double curvatureAt(double t) {
        Preconditions.requireFinite(t, "t");
        // Curvature for parabola y = x^2/(4p): k = 1/(2p*(1 + t^2)^3/2)
        // Parametric form: k = 1 / (2*p*(1 + t^2)^1.5)
        double denom = Math.pow(1.0 + t * t, 1.5);
        return 1.0 / (2.0 * focalDistance * denom);
    }

    /**
     * Returns the closest point on the parabola to a given point.
     *
     * @param point target point
     * @return closest point on the parabola (approximately)
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto parabola plane
        Vector3 offset = point.subtract(position.location());
        double planeDistance = offset.dot(position.axis().asVector());
        Vector3 axialOffset = position.axis().asVector().scale(planeDistance);
        CartesianPoint projected = new CartesianPoint(
            point.x() - axialOffset.x(),
            point.y() - axialOffset.y(),
            point.z() - axialOffset.z()
        );

        // Find approximate parameter from projected point
        Vector3 radialOffset = projected.subtract(position.location());
        double x = radialOffset.dot(position.xDirection().asVector());
        double y = radialOffset.dot(position.yDirection().asVector());

        // For parabola x = 2*p*t, y = p*t^2, solve t approximately
        double tApprox = x / (2.0 * focalDistance);
        return pointAt(tApprox);
    }

    /**
     * Returns the distance from a point to the parabola.
     *
     * @param point target point
     * @return approximate distance to the parabola
     */
    @Override
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the focus point of the parabola.
     *
     * @return focus point
     */
    public CartesianPoint focus() {
        return position.location().add(position.yDirection().asVector().scale(focalDistance));
    }

    /**
     * Returns the vertex point of the parabola.
     *
     * @return vertex point (same as position location)
     */
    public CartesianPoint vertex() {
        return position.location();
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
     * Returns the curve parameter corresponding to the given point.
     * For a parabola with parametric form x = 2*p*t, the parameter t = x / (2*p).
     *
     * @param point a point on or near the parabola
     * @return parameter value t
     */
    @Override
    public double parameterAt(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(position.location());
        double x = offset.dot(position.xDirection().asVector());
        return x / (2.0 * focalDistance);
    }
}