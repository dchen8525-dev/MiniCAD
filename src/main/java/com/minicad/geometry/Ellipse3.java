package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 3D ellipse representation.
 *
 * @param position ellipse placement
 * @param semiAxis1 semi-axis along local X
 * @param semiAxis2 semi-axis along local Y
 */
public record Ellipse3(Axis2Placement3D position, double semiAxis1, double semiAxis2) implements Curve3 {

    /**
     * Creates an ellipse and validates its invariants.
     */
    public Ellipse3 {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(semiAxis1, "semiAxis1");
        Preconditions.requireFinite(semiAxis2, "semiAxis2");
        if (semiAxis1 <= Epsilon.EPS || semiAxis2 <= Epsilon.EPS) {
            throw new GeometryException("ellipse semi-axes must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the ellipse.
     *
     * @param angle angle in radians from local X
     * @return point on the ellipse
     */
    public CartesianPoint pointAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector3 offset = position.xDirection().asVector().scale(Math.cos(angle) * semiAxis1)
                .add(position.yDirection().asVector().scale(Math.sin(angle) * semiAxis2));
        return position.location().add(offset);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(position.location());
        double planeDistance = Math.abs(offset.dot(position.axis().asVector()));
        if (planeDistance > Epsilon.EPS) {
            return false;
        }
        double x = offset.dot(position.xDirection().asVector()) / semiAxis1;
        double y = offset.dot(position.yDirection().asVector()) / semiAxis2;
        return Epsilon.equals(x * x + y * y, 1.0);
    }

    /**
     * Returns the natural parameter angle for a point on the ellipse.
     *
     * @param point point on the ellipse
     * @return angle in radians in [0, 2pi)
     */
    public double angleOf(CartesianPoint point) {
        if (!contains(point)) {
            throw new GeometryException("point must lie on the ellipse");
        }
        Vector3 offset = point.subtract(position.location());
        double x = offset.dot(position.xDirection().asVector()) / semiAxis1;
        double y = offset.dot(position.yDirection().asVector()) / semiAxis2;
        double angle = Math.atan2(y, x);
        return angle >= 0.0 ? angle : angle + Math.PI * 2.0;
    }

    /**
     * Samples the ellipse at discrete points.
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
     * Samples an arc of the ellipse at discrete points.
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
     * Computes the tangent vector at a given angle on the ellipse.
     *
     * @param angle angle in radians from local X
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        // Derivative: -sin(angle) * semiAxis1 * xDir + cos(angle) * semiAxis2 * yDir
        Vector3 tangent = position.xDirection().asVector().scale(-Math.sin(angle) * semiAxis1)
                .add(position.yDirection().asVector().scale(Math.cos(angle) * semiAxis2));
        if (tangent.norm() <= Epsilon.EPS) {
            return position.yDirection().asVector();
        }
        return tangent.normalize().asVector();
    }

    /**
     * Computes the normal vector in the ellipse plane at a given angle.
     *
     * @param angle angle in radians from local X
     * @return unit normal vector in the plane of the ellipse
     */
    public Vector3 normalInPlaneAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        // For ellipse, inward normal proportional to (cos(angle)/a^2, sin(angle)/b^2)
        double nx = Math.cos(angle) / (semiAxis1 * semiAxis1);
        double ny = Math.sin(angle) / (semiAxis2 * semiAxis2);
        Vector3 normal = position.xDirection().asVector().scale(nx)
                .add(position.yDirection().asVector().scale(ny));
        if (normal.norm() <= Epsilon.EPS) {
            return position.xDirection().asVector();
        }
        return normal.normalize().asVector();
    }

    /**
     * Computes the curvature at a given angle on the ellipse.
     * Curvature formula: k = (a*b) / (a^2*sin^2 + b^2*cos^2)^(3/2)
     *
     * @param angle angle in radians
     * @return curvature
     */
    public double curvatureAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        double a = semiAxis1;
        double b = semiAxis2;
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        double denominator = Math.pow(a * a * sinAngle * sinAngle + b * b * cosAngle * cosAngle, 1.5);
        return (a * b) / denominator;
    }

    /**
     * Returns the binormal vector (surface normal perpendicular to ellipse plane).
     *
     * @param angle angle in radians
     * @return unit binormal vector
     */
    public Vector3 binormalAt(double angle) {
        return position.axis().asVector();
    }

    /**
     * Approximates the perimeter of the ellipse using Ramanujan's formula.
     *
     * @return approximate perimeter
     */
    public double perimeter() {
        double a = semiAxis1;
        double b = semiAxis2;
        double h = Math.pow((a - b) / (a + b), 2);
        return Math.PI * (a + b) * (1.0 + 3.0 * h / (10.0 + Math.sqrt(4.0 - 3.0 * h)));
    }

    /**
     * Returns the bounding box of the full ellipse.
     *
     * @return bounding box enclosing the entire ellipse
     */
    public BoundingBox3 boundingBox() {
        CartesianPoint center = position.location();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();

        // The ellipse extends semiAxis1 in xDir direction and semiAxis2 in yDir direction
        CartesianPoint min = new CartesianPoint(
            center.x() - semiAxis1 * Math.abs(xDir.x()) - semiAxis2 * Math.abs(yDir.x()),
            center.y() - semiAxis1 * Math.abs(xDir.y()) - semiAxis2 * Math.abs(yDir.y()),
            center.z() - semiAxis1 * Math.abs(xDir.z()) - semiAxis2 * Math.abs(yDir.z())
        );
        CartesianPoint max = new CartesianPoint(
            center.x() + semiAxis1 * Math.abs(xDir.x()) + semiAxis2 * Math.abs(yDir.x()),
            center.y() + semiAxis1 * Math.abs(xDir.y()) + semiAxis2 * Math.abs(yDir.y()),
            center.z() + semiAxis1 * Math.abs(xDir.z()) + semiAxis2 * Math.abs(yDir.z())
        );
        return BoundingBox3.of(min, max);
    }

    /**
     * Returns the bounding box of an arc of the ellipse.
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
     * Returns an approximate closest point on the ellipse to a given point.
     * This uses a simple iterative approach for the parametric form.
     *
     * @param point target point
     * @return approximate closest point on the ellipse
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto ellipse plane
        Vector3 offset = point.subtract(position.location());
        double planeDistance = offset.dot(position.axis().asVector());
        Vector3 axialOffset = position.axis().asVector().scale(planeDistance);
        CartesianPoint projected = new CartesianPoint(
            point.x() - axialOffset.x(),
            point.y() - axialOffset.y(),
            point.z() - axialOffset.z()
        );

        // Find the angle from center to projected point (approximate)
        Vector3 radialOffset = projected.subtract(position.location());
        double x = radialOffset.dot(position.xDirection().asVector());
        double y = radialOffset.dot(position.yDirection().asVector());
        double angle = Math.atan2(y / semiAxis2, x / semiAxis1);

        return pointAt(angle);
    }

    /**
     * Returns an approximate distance from a point to the ellipse.
     *
     * @param point target point
     * @return approximate shortest distance to the ellipse
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }
}
