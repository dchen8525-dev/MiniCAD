package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 3D circle representation.
 *
 * @param position circle placement
 * @param radius positive radius
 */
public record Circle(Axis2Placement3D position, double radius) implements Curve3 {

    /**
     * Creates a circle and validates its invariants.
     */
    public Circle {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("radius must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the circle using the placement's natural orientation.
     *
     * @param angle angle in radians from the local X axis
     * @return point on the circle
     */
    @Override
    public CartesianPoint pointAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector3 radial = position.xDirection().asVector().scale(Math.cos(angle) * radius)
                .add(position.yDirection().asVector().scale(Math.sin(angle) * radius));
        return position.location().add(radial);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(position.location());
        double planeDistance = Math.abs(offset.dot(position.axis().asVector()));
        if (planeDistance > Epsilon.EPS) {
            return false;
        }
        return Epsilon.equals(offset.norm(), radius);
    }

    /**
     * Returns the natural parameter angle for a point on the circle.
     *
     * @param point point on the circle
     * @return angle in radians in [0, 2pi)
     */
    public double angleOf(CartesianPoint point) {
        if (!contains(point)) {
            throw new GeometryException("point must lie on the circle");
        }
        Vector3 offset = point.subtract(position.location());
        double x = offset.dot(position.xDirection().asVector());
        double y = offset.dot(position.yDirection().asVector());
        double angle = Math.atan2(y, x);
        return angle >= 0.0 ? angle : angle + Math.PI * 2.0;
    }

    /**
     * Samples the circle at discrete points.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            points.add(pointAt(angle));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Samples an arc of the circle at discrete points.
     *
     * @param segments number of segments
     * @param startAngle start angle in radians
     * @param endAngle end angle in radians
     * @return list of sampled points
     */
    public java.util.List<CartesianPoint> sample(int segments, double startAngle, double endAngle) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + (endAngle - startAngle) * i / segments;
            points.add(pointAt(angle));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Computes the tangent vector at a given angle on the circle.
     * The tangent is perpendicular to the radius and points in the direction of increasing angle.
     *
     * @param angle angle in radians from the local X axis
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        // Tangent is in the direction: -sin(angle) * xDir + cos(angle) * yDir
        Vector3 tangent = position.xDirection().asVector().scale(-Math.sin(angle))
                .add(position.yDirection().asVector().scale(Math.cos(angle)));
        return tangent.normalize().asVector();
    }

    /**
     * Computes the normal vector at a given angle on the circle.
     * The normal is perpendicular to the tangent and points outward from the circle center.
     *
     * @param angle angle in radians from the local X axis
     * @return unit normal vector in the plane of the circle
     */
    public Vector3 normalInPlaneAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        // Normal in plane is in the direction: cos(angle) * xDir + sin(angle) * yDir
        Vector3 normal = position.xDirection().asVector().scale(Math.cos(angle))
                .add(position.yDirection().asVector().scale(Math.sin(angle)));
        return normal.normalize().asVector();
    }

    /**
     * Returns the curvature of the circle.
     * For a circle, curvature is constant and equal to 1/radius.
     *
     * @return curvature (1/radius)
     */
    public double curvature() {
        return 1.0 / radius;
    }

    /**
     * Returns the curvature at any point on the circle.
     *
     * @param angle angle in radians (curvature is independent of angle)
     * @return curvature (1/radius)
     */
    public double curvatureAt(double angle) {
        return curvature();
    }

    /**
     * Returns the binormal vector at a given angle.
     * The binormal is the surface normal, perpendicular to the circle plane.
     *
     * @param angle angle in radians
     * @return unit binormal vector (same as circle plane normal)
     */
    public Vector3 binormalAt(double angle) {
        return position.axis().asVector();
    }

    /**
     * Computes the arc length between two angles.
     *
     * @param startAngle start angle in radians
     * @param endAngle end angle in radians
     * @return arc length
     */
    public double arcLength(double startAngle, double endAngle) {
        Preconditions.requireFinite(startAngle, "startAngle");
        Preconditions.requireFinite(endAngle, "endAngle");
        return Math.abs(endAngle - startAngle) * radius;
    }

    /**
     * Returns the total circumference of the circle.
     *
     * @return circumference (2*PI*radius)
     */
    public double circumference() {
        return 2.0 * Math.PI * radius;
    }

    /**
     * Returns the bounding box of the full circle.
     *
     * @return bounding box enclosing the entire circle
     */
    public BoundingBox3 boundingBox() {
        CartesianPoint center = position.location();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        Vector3 zDir = position.axis().asVector();

        // The circle extends radius in each local direction
        CartesianPoint min = new CartesianPoint(
            center.x() - radius * (Math.abs(xDir.x()) + Math.abs(yDir.x())),
            center.y() - radius * (Math.abs(xDir.y()) + Math.abs(yDir.y())),
            center.z() - radius * (Math.abs(xDir.z()) + Math.abs(yDir.z()))
        );
        CartesianPoint max = new CartesianPoint(
            center.x() + radius * (Math.abs(xDir.x()) + Math.abs(yDir.x())),
            center.y() + radius * (Math.abs(xDir.y()) + Math.abs(yDir.y())),
            center.z() + radius * (Math.abs(xDir.z()) + Math.abs(yDir.z()))
        );
        return BoundingBox3.of(min, max);
    }

    /**
     * Returns the bounding box of an arc of the circle.
     *
     * @param startAngle start angle in radians
     * @param endAngle end angle in radians
     * @return bounding box enclosing the arc
     */
    public BoundingBox3 boundingBox(double startAngle, double endAngle) {
        Preconditions.requireFinite(startAngle, "startAngle");
        Preconditions.requireFinite(endAngle, "endAngle");

        // Sample the arc and build bounding box from sampled points
        BoundingBox3 box = BoundingBox3.empty();
        int segments = Math.max(8, (int) Math.ceil(Math.abs(endAngle - startAngle) / (Math.PI / 8)));
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + (endAngle - startAngle) * i / segments;
            box = box.union(pointAt(angle));
        }
        return box;
    }

    /**
     * Returns the closest point on the circle to a given point.
     * The closest point is in the plane of the circle, at the angle
     * corresponding to the projection of the given point onto the circle plane.
     *
     * @param point target point
     * @return closest point on the circle
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto circle plane
        Vector3 offset = point.subtract(position.location());
        double planeDistance = offset.dot(position.axis().asVector());
        Vector3 axialOffset = position.axis().asVector().scale(planeDistance);
        CartesianPoint projected = new CartesianPoint(
            point.x() - axialOffset.x(),
            point.y() - axialOffset.y(),
            point.z() - axialOffset.z()
        );

        // Find the angle from center to projected point
        Vector3 radialOffset = projected.subtract(position.location());
        double x = radialOffset.dot(position.xDirection().asVector());
        double y = radialOffset.dot(position.yDirection().asVector());
        double angle = Math.atan2(y, x);

        return pointAt(angle);
    }

    /**
     * Returns the shortest distance from a point to the circle.
     *
     * @param point target point
     * @return shortest distance to the circle
     */
    @Override
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }
}
