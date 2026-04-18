package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal infinite cylindrical surface representation.
 *
 * @param position cylinder placement
 * @param radius positive radius
 */
public record CylindricalSurface(Axis2Placement3D position, double radius) implements SurfaceGeometry {

    /**
     * Creates a cylindrical surface and validates its invariants.
     */
    public CylindricalSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("radius must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the cylinder surface.
     *
     * @param angle angle around the axis in radians
     * @param height height along the axis from the position origin
     * @return point on the cylinder
     */
    public CartesianPoint pointAt(double angle, double height) {
        Preconditions.requireFinite(angle, "angle");
        Preconditions.requireFinite(height, "height");
        Vector3 radial = position.xDirection().asVector().scale(Math.cos(angle) * radius)
                .add(position.yDirection().asVector().scale(Math.sin(angle) * radius));
        Vector3 axial = position.axis().asVector().scale(height);
        return position.location().add(radial).add(axial);
    }

    /**
     * Samples a patch on the cylindrical surface.
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
     * Samples a full cylinder patch.
     *
     * @param angleSegments number of segments around the circumference
     * @param heightSegments number of segments along the height
     * @return grid of sampled points (full circumference)
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int angleSegments, int heightSegments) {
        return sampleGrid(angleSegments, heightSegments, -1.0, 1.0);
    }

    /**
     * Computes the normal at a given angle on the cylinder surface.
     * The normal points outward from the cylinder axis.
     *
     * @param angle angle around the axis in radians
     * @return unit normal vector (outward radial direction)
     */
    public Vector3 normalAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector3 radial = position.xDirection().asVector().scale(Math.cos(angle))
                .add(position.yDirection().asVector().scale(Math.sin(angle)));
        return radial.normalize().asVector();
    }

    @Override
    public Vector3 normalAt(double u, double v) {
        return normalAt(u);
    }

    @Override
    public BoundingBox3 boundingBox() {
        return boundingBox(-1.0, 1.0);
    }

    /**
     * Returns the bounding box for a cylindrical patch.
     *
     * @param heightMin minimum height along axis
     * @param heightMax maximum height along axis
     * @return bounding box enclosing the cylinder patch (full circumference)
     */
    public BoundingBox3 boundingBox(double heightMin, double heightMax) {
        Preconditions.requireFinite(heightMin, "heightMin");
        Preconditions.requireFinite(heightMax, "heightMax");
        CartesianPoint base = position.location();
        Vector3 axisVec = position.axis().asVector();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();

        // Cylinder extends radius in radial directions (xDir and yDir)
        // and heightMin/heightMax in axial direction
        double axialMin = Math.min(heightMin, heightMax);
        double axialMax = Math.max(heightMin, heightMax);

        CartesianPoint min = new CartesianPoint(
            base.x() - radius * (Math.abs(xDir.x()) + Math.abs(yDir.x())) + axialMin * axisVec.x(),
            base.y() - radius * (Math.abs(xDir.y()) + Math.abs(yDir.y())) + axialMin * axisVec.y(),
            base.z() - radius * (Math.abs(xDir.z()) + Math.abs(yDir.z())) + axialMin * axisVec.z()
        );
        CartesianPoint max = new CartesianPoint(
            base.x() + radius * (Math.abs(xDir.x()) + Math.abs(yDir.x())) + axialMax * axisVec.x(),
            base.y() + radius * (Math.abs(xDir.y()) + Math.abs(yDir.y())) + axialMax * axisVec.y(),
            base.z() + radius * (Math.abs(xDir.z()) + Math.abs(yDir.z())) + axialMax * axisVec.z()
        );
        return BoundingBox3.of(min, max);
    }

    /**
     * Returns the bounding box for a partial cylindrical patch.
     *
     * @param angleStart start angle in radians
     * @param angleEnd end angle in radians
     * @param heightMin minimum height
     * @param heightMax maximum height
     * @return bounding box for the patch
     */
    public BoundingBox3 boundingBox(double angleStart, double angleEnd, double heightMin, double heightMax) {
        Preconditions.requireFinite(angleStart, "angleStart");
        Preconditions.requireFinite(angleEnd, "angleEnd");
        Preconditions.requireFinite(heightMin, "heightMin");
        Preconditions.requireFinite(heightMax, "heightMax");

        BoundingBox3 box = BoundingBox3.empty();
        int angleSegments = Math.max(4, (int) Math.ceil(Math.abs(angleEnd - angleStart) / (Math.PI / 8)));
        int heightSegments = Math.max(2, (int) Math.ceil(Math.abs(heightMax - heightMin)));

        for (int i = 0; i <= angleSegments; i++) {
            double angle = angleStart + (angleEnd - angleStart) * i / angleSegments;
            for (int j = 0; j <= heightSegments; j++) {
                double height = heightMin + (heightMax - heightMin) * j / heightSegments;
                box = box.union(pointAt(angle, height));
            }
        }
        return box;
    }

    /**
     * Returns the closest point on the cylinder surface to a given point.
     * Projects the point onto the cylinder axis and then extends radially.
     *
     * @param point target point
     * @return closest point on the cylinder surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto cylinder axis
        Vector3 offset = point.subtract(position.location());
        double axialProjection = offset.dot(position.axis().asVector());
        Vector3 axialOffset = position.axis().asVector().scale(axialProjection);
        CartesianPoint axialPoint = position.location().add(axialOffset);

        // Find radial direction from axis to the projected point
        Vector3 radial = point.subtract(axialPoint);
        if (radial.norm() <= Epsilon.EPS) {
            // Point is on axis, any radial direction works
            return pointAt(0, axialProjection);
        }

        // Find the angle from the radial direction
        double x = radial.dot(position.xDirection().asVector());
        double y = radial.dot(position.yDirection().asVector());
        double angle = Math.atan2(y, x);

        return pointAt(angle, axialProjection);
    }

    /**
     * Returns the shortest distance from a point to the cylinder surface.
     *
     * @param point target point
     * @return shortest distance to the cylinder surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }
}
