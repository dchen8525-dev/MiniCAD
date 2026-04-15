package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Infinite 3D plane defined by a point and a unit normal.
 *
 * @param origin point on the plane
 * @param normal unit plane normal
 */
public record Plane(CartesianPoint origin, Direction3 normal) implements SurfaceGeometry {

    /**
     * Creates a plane and validates its fields.
     *
     */
    public Plane {
        Preconditions.requireNonNull(origin, "origin");
        Preconditions.requireNonNull(normal, "normal");
    }

    /**
     * Returns the signed distance from a point to the plane.
     *
     * @param point queried point
     * @return signed distance
     */
    public double signedDistanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.subtract(origin).dot(normal.asVector());
    }

    /**
     * Returns the absolute distance from a point to the plane.
     *
     * @param point queried point
     * @return absolute distance
     */
    public double distanceTo(CartesianPoint point) {
        return Math.abs(signedDistanceTo(point));
    }

    /**
     * Returns whether a point lies on the plane within epsilon.
     *
     * @param point queried point
     * @return whether the point lies on the plane
     */
    public boolean contains(CartesianPoint point) {
        return Epsilon.isZero(signedDistanceTo(point));
    }

    /**
     * Intersects this plane with a line.
     *
     * @param line line to intersect
     * @return intersection point
     */
    public CartesianPoint intersect(Line3 line) {
        Preconditions.requireNonNull(line, "line");
        double denominator = normal.asVector().dot(line.direction().asVector());
        if (Epsilon.isZero(denominator)) {
            throw new GeometryException("line is parallel to plane");
        }
        double t = -signedDistanceTo(line.origin()) / denominator;
        return line.pointAt(t);
    }

    /**
     * Returns the normal vector of the plane.
     * The normal is constant everywhere on a plane.
     *
     * @return unit normal vector
     */
    public Vector3 normalAt() {
        return normal.asVector();
    }

    /**
     * Samples a rectangular patch on the plane.
     *
     * @param placement local coordinate system on the plane
     * @param xMin minimum X coordinate in local frame
     * @param xMax maximum X coordinate in local frame
     * @param yMin minimum Y coordinate in local frame
     * @param yMax maximum Y coordinate in local frame
     * @param xSegments number of segments along X
     * @param ySegments number of segments along Y
     * @return grid of sampled points
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(
            Axis2Placement3D placement,
            double xMin, double xMax,
            double yMin, double yMax,
            int xSegments, int ySegments) {
        Preconditions.requireNonNull(placement, "placement");
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(xSegments + 1);
        for (int i = 0; i <= xSegments; i++) {
            double x = xMin + (xMax - xMin) * i / xSegments;
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>(ySegments + 1);
            for (int j = 0; j <= ySegments; j++) {
                double y = yMin + (yMax - yMin) * j / ySegments;
                Vector3 offset = placement.xDirection().asVector().scale(x)
                        .add(placement.yDirection().asVector().scale(y));
                row.add(placement.location().add(offset));
            }
            grid.add(java.util.List.copyOf(row));
        }
        return java.util.List.copyOf(grid);
    }

    /**
     * Samples a rectangular patch on the plane using the origin as center.
     *
     * @param xSize half-size along local X
     * @param ySize half-size along local Y
     * @param xSegments number of segments along X
     * @param ySegments number of segments along Y
     * @return grid of sampled points centered at origin
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(
            double xSize, double ySize,
            int xSegments, int ySegments) {
        // Create a default axis placement from the plane origin and normal
        Direction3 xDir = findPerpendicularDirection(normal);
        Direction3 yDir = normal.asVector().cross(xDir.asVector()).normalize();
        Axis2Placement3D placement = new Axis2Placement3D(origin, xDir, yDir);
        return sampleGrid(placement, -xSize, xSize, -ySize, ySize, xSegments, ySegments);
    }

    /**
     * Finds a perpendicular direction to the given normal.
     */
    private static Direction3 findPerpendicularDirection(Direction3 normal) {
        // Find a direction perpendicular to the normal
        Vector3 n = normal.asVector();
        // Try different candidates
        Vector3 candidate1 = new Vector3(1, 0, 0);
        if (Math.abs(n.dot(candidate1)) < 0.9) {
            return n.cross(candidate1).normalize();
        }
        Vector3 candidate2 = new Vector3(0, 1, 0);
        if (Math.abs(n.dot(candidate2)) < 0.9) {
            return n.cross(candidate2).normalize();
        }
        Vector3 candidate3 = new Vector3(0, 0, 1);
        return n.cross(candidate3).normalize();
    }

    /**
     * Returns the closest point on the plane to a given point.
     *
     * @param point target point
     * @return closest point on the plane
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.projectOnto(this);
    }

    /**
     * Projects a point onto the plane.
     *
     * @param point point to project
     * @return projected point on the plane
     */
    public CartesianPoint project(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return closestPointTo(point);
    }
}
