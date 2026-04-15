package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal infinite conical surface representation.
 *
 * @param position cone placement
 * @param radius radius at placement origin
 * @param semiAngle cone semi-angle in radians
 */
public record ConicalSurface(Axis2Placement3D position, double radius, double semiAngle) implements SurfaceGeometry {

    /**
     * Creates a conical surface and validates its invariants.
     */
    public ConicalSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        Preconditions.requireFinite(semiAngle, "semiAngle");
        if (radius <= 0.0) {
            throw new GeometryException("radius must be positive");
        }
        if (Math.abs(semiAngle) >= Math.PI / 2.0) {
            throw new GeometryException("semiAngle magnitude must be less than pi/2");
        }
    }

    /**
     * Evaluates a point on the cone surface.
     *
     * @param angle angle around the axis in radians
     * @param height height along the axis from the position origin
     * @return point on the cone
     */
    public CartesianPoint pointAt(double angle, double height) {
        Preconditions.requireFinite(angle, "angle");
        Preconditions.requireFinite(height, "height");
        double r = radius + height * Math.tan(semiAngle);
        Vector3 radial = position.xDirection().asVector().scale(Math.cos(angle) * r)
                .add(position.yDirection().asVector().scale(Math.sin(angle) * r));
        Vector3 axial = position.axis().asVector().scale(height);
        return position.location().add(radial).add(axial);
    }

    /**
     * Samples a patch on the conical surface.
     *
     * @param angleSegments number of segments around the circumference
     * @param heightSegments number of segments along the height
     * @param heightMin minimum height
     * @param heightMax maximum height
     * @return grid of sampled points
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(
            int angleSegments, int heightSegments,
            double heightMin, double heightMax) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(angleSegments + 1);
        for (int i = 0; i <= angleSegments; i++) {
            double angle = 2.0 * Math.PI * i / angleSegments;
            java.util.List<CartesianPoint> column = new java.util.ArrayList<>(heightSegments + 1);
            for (int j = 0; j <= heightSegments; j++) {
                double height = heightMin + (heightMax - heightMin) * j / heightSegments;
                column.add(pointAt(angle, height));
            }
            grid.add(java.util.List.copyOf(column));
        }
        return java.util.List.copyOf(grid);
    }

    /**
     * Samples a full cone patch (full circumference, default height range).
     *
     * @param angleSegments number of segments around the circumference
     * @param heightSegments number of segments along the height
     * @return grid of sampled points (full circumference)
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int angleSegments, int heightSegments) {
        return sampleGrid(angleSegments, heightSegments, -1.0, 1.0);
    }

    /**
     * Computes the normal at a given angle on the cone surface.
     * The normal is perpendicular to the cone surface, pointing outward.
     *
     * @param angle angle around the axis in radians
     * @return unit normal vector (perpendicular to cone surface)
     */
    public Vector3 normalAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        double radialX = Math.cos(angle);
        double radialY = Math.sin(angle);
        double axial = -Math.tan(semiAngle);
        Vector3 localNormal = new Vector3(radialX, radialY, axial);
        Vector3 worldNormal = position.xDirection().asVector().scale(localNormal.x())
                .add(position.yDirection().asVector().scale(localNormal.y()))
                .add(position.axis().asVector().scale(localNormal.z()));
        return worldNormal.normalize().asVector();
    }

    /**
     * Returns the bounding box for a conical patch.
     *
     * @param heightMin minimum height along axis
     * @param heightMax maximum height along axis
     * @return bounding box enclosing the cone patch (full circumference)
     */
    public BoundingBox3 boundingBox(double heightMin, double heightMax) {
        Preconditions.requireFinite(heightMin, "heightMin");
        Preconditions.requireFinite(heightMax, "heightMax");

        // Cone radius at each height: r = radius + height * tan(semiAngle)
        double rMin = radius + heightMin * Math.tan(semiAngle);
        double rMax = radius + heightMax * Math.tan(semiAngle);
        double maxRadius = Math.max(Math.abs(rMin), Math.abs(rMax));

        CartesianPoint base = position.location();
        Vector3 zDir = position.axis().asVector();

        CartesianPoint min = new CartesianPoint(
            base.x() - maxRadius + Math.min(0, heightMin) * zDir.x(),
            base.y() - maxRadius + Math.min(0, heightMin) * zDir.y(),
            base.z() - maxRadius + Math.min(0, heightMin) * zDir.z()
        );
        CartesianPoint max = new CartesianPoint(
            base.x() + maxRadius + Math.max(0, heightMax) * zDir.x(),
            base.y() + maxRadius + Math.max(0, heightMax) * zDir.y(),
            base.z() + maxRadius + Math.max(0, heightMax) * zDir.z()
        );
        return BoundingBox3.of(min, max);
    }

    /**
     * Returns an approximate closest point on the cone surface to a given point.
     * Uses iterative approach to find the closest point.
     *
     * @param point target point
     * @return approximate closest point on the cone surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto cone axis to find height
        Vector3 offset = point.subtract(position.location());
        double height = offset.dot(position.axis().asVector());

        // Find radius at this height
        double coneRadius = radius + height * Math.tan(semiAngle);

        // Find radial direction from axis to the projected point
        Vector3 axialOffset = position.axis().asVector().scale(height);
        CartesianPoint axialPoint = position.location().add(axialOffset);
        Vector3 radial = point.subtract(axialPoint);

        if (radial.norm() <= com.minicad.common.Epsilon.EPS) {
            // Point is on axis, return any point at this height
            return pointAt(0, height);
        }

        // Find the angle from the radial direction
        double x = radial.dot(position.xDirection().asVector());
        double y = radial.dot(position.yDirection().asVector());
        double angle = Math.atan2(y, x);

        return pointAt(angle, height);
    }

    /**
     * Returns an approximate shortest distance from a point to the cone surface.
     *
     * @param point target point
     * @return approximate shortest distance to the cone surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }
}
