package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal spherical surface representation.
 *
 * @param position sphere placement
 * @param radius sphere radius
 */
public record SphericalSurface(Axis2Placement3D position, double radius) implements SurfaceGeometry {

    /**
     * Creates a spherical surface and validates its invariants.
     */
    public SphericalSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("sphere radius must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the sphere surface.
     *
     * @param theta azimuthal angle in radians (longitude)
     * @param phi polar angle in radians (colatitude from axis)
     * @return point on the sphere
     */
    public CartesianPoint pointAt(double theta, double phi) {
        Preconditions.requireFinite(theta, "theta");
        Preconditions.requireFinite(phi, "phi");
        double x = radius * Math.sin(phi) * Math.cos(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(phi);
        Vector3 local = new Vector3(x, y, z);
        Vector3 offset = position.xDirection().asVector().scale(local.x())
                .add(position.yDirection().asVector().scale(local.y()))
                .add(position.axis().asVector().scale(local.z()));
        return position.location().add(offset);
    }

    /**
     * Samples a patch on the spherical surface.
     *
     * @param thetaSegments number of segments around the azimuth
     * @param phiSegments number of segments along the polar angle
     * @param phiMin minimum polar angle in radians
     * @param phiMax maximum polar angle in radians
     * @return grid of sampled points
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(
            int thetaSegments, int phiSegments,
            double phiMin, double phiMax) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(thetaSegments + 1);
        for (int i = 0; i <= thetaSegments; i++) {
            double theta = 2.0 * Math.PI * i / thetaSegments;
            java.util.List<CartesianPoint> column = new java.util.ArrayList<>(phiSegments + 1);
            for (int j = 0; j <= phiSegments; j++) {
                double phi = phiMin + (phiMax - phiMin) * j / phiSegments;
                column.add(pointAt(theta, phi));
            }
            grid.add(java.util.List.copyOf(column));
        }
        return java.util.List.copyOf(grid);
    }

    /**
     * Samples a full sphere patch (full azimuth, polar range from 0 to pi).
     *
     * @param thetaSegments number of segments around the azimuth
     * @param phiSegments number of segments along the polar angle
     * @return grid of sampled points (full sphere)
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int thetaSegments, int phiSegments) {
        return sampleGrid(thetaSegments, phiSegments, 0.0, Math.PI);
    }

    /**
     * Computes the normal at a given position on the sphere surface.
     * The normal points outward from the sphere center.
     *
     * @param theta azimuthal angle in radians (longitude)
     * @param phi polar angle in radians (colatitude from axis)
     * @return unit normal vector (outward from center)
     */
    public Vector3 normalAt(double theta, double phi) {
        Preconditions.requireFinite(theta, "theta");
        Preconditions.requireFinite(phi, "phi");
        double x = Math.sin(phi) * Math.cos(theta);
        double y = Math.sin(phi) * Math.sin(theta);
        double z = Math.cos(phi);
        Vector3 localNormal = new Vector3(x, y, z);
        Vector3 worldNormal = position.xDirection().asVector().scale(localNormal.x())
                .add(position.yDirection().asVector().scale(localNormal.y()))
                .add(position.axis().asVector().scale(localNormal.z()));
        return worldNormal.normalize().asVector();
    }

    /**
     * Returns the bounding box for the full sphere.
     *
     * @return bounding box enclosing the entire sphere
     */
    public BoundingBox3 boundingBox() {
        CartesianPoint center = position.location();
        CartesianPoint min = new CartesianPoint(
            center.x() - radius,
            center.y() - radius,
            center.z() - radius
        );
        CartesianPoint max = new CartesianPoint(
            center.x() + radius,
            center.y() + radius,
            center.z() + radius
        );
        return BoundingBox3.of(min, max);
    }

    /**
     * Returns the bounding box for a partial spherical patch.
     *
     * @param thetaMin minimum theta angle (azimuth)
     * @param thetaMax maximum theta angle
     * @param phiMin minimum phi angle (polar)
     * @param phiMax maximum phi angle
     * @return bounding box for the patch
     */
    public BoundingBox3 boundingBox(double thetaMin, double thetaMax, double phiMin, double phiMax) {
        Preconditions.requireFinite(thetaMin, "thetaMin");
        Preconditions.requireFinite(thetaMax, "thetaMax");
        Preconditions.requireFinite(phiMin, "phiMin");
        Preconditions.requireFinite(phiMax, "phiMax");

        BoundingBox3 box = BoundingBox3.empty();
        int thetaSegs = Math.max(8, (int) Math.ceil(Math.abs(thetaMax - thetaMin) / (Math.PI / 8)));
        int phiSegs = Math.max(8, (int) Math.ceil(Math.abs(phiMax - phiMin) / (Math.PI / 8)));

        for (int i = 0; i <= thetaSegs; i++) {
            double theta = thetaMin + (thetaMax - thetaMin) * i / thetaSegs;
            for (int j = 0; j <= phiSegs; j++) {
                double phi = phiMin + (phiMax - phiMin) * j / phiSegs;
                box = box.union(pointAt(theta, phi));
            }
        }
        return box;
    }

    /**
     * Returns the closest point on the sphere surface to a given point.
     * Projects the point radially from the sphere center onto the surface.
     *
     * @param point target point
     * @return closest point on the sphere surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(position.location());
        double distance = offset.norm();
        if (distance <= Epsilon.EPS) {
            // Point is at center, return any point on surface
            return pointAt(0, 0);
        }

        // Normalize and scale to radius
        Direction3 radial = offset.normalize();
        double theta = Math.atan2(
            radial.asVector().dot(position.yDirection().asVector()),
            radial.asVector().dot(position.xDirection().asVector())
        );
        double phi = Math.acos(radial.asVector().dot(position.axis().asVector()));

        return pointAt(theta, phi);
    }

    /**
     * Returns the shortest distance from a point to the sphere surface.
     *
     * @param point target point
     * @return shortest distance to the sphere surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        double distanceToCenter = point.subtract(position.location()).norm();
        return Math.abs(distanceToCenter - radius);
    }
}
