package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal toroidal surface representation.
 *
 * @param position torus placement
 * @param majorRadius distance from axis to tube center
 * @param minorRadius tube radius
 */
public record ToroidalSurface(Axis2Placement3D position, double majorRadius, double minorRadius) implements SurfaceGeometry {

    /**
     * Creates a toroidal surface and validates its invariants.
     */
    public ToroidalSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(majorRadius, "majorRadius");
        Preconditions.requireFinite(minorRadius, "minorRadius");
        if (majorRadius <= Epsilon.EPS || minorRadius <= Epsilon.EPS) {
            throw new GeometryException("toroidal radii must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the torus surface.
     *
     * @param theta angle around the major axis in radians
     * @param phi angle around the tube cross-section in radians
     * @return point on the torus
     */
    public CartesianPoint pointAt(double theta, double phi) {
        Preconditions.requireFinite(theta, "theta");
        Preconditions.requireFinite(phi, "phi");
        double tubeCenter = majorRadius + minorRadius * Math.cos(phi);
        double x = tubeCenter * Math.cos(theta);
        double y = tubeCenter * Math.sin(theta);
        double z = minorRadius * Math.sin(phi);
        Vector3 local = new Vector3(x, y, z);
        Vector3 offset = position.xDirection().asVector().scale(local.x())
                .add(position.yDirection().asVector().scale(local.y()))
                .add(position.axis().asVector().scale(local.z()));
        return position.location().add(offset);
    }

    /**
     * Samples a patch on the toroidal surface.
     *
     * @param thetaSegments number of segments around the major axis
     * @param phiSegments number of segments around the tube cross-section
     * @param thetaMin minimum theta angle in radians
     * @param thetaMax maximum theta angle in radians
     * @param phiMin minimum phi angle in radians
     * @param phiMax maximum phi angle in radians
     * @return grid of sampled points
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(
            int thetaSegments, int phiSegments,
            double thetaMin, double thetaMax,
            double phiMin, double phiMax) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(thetaSegments + 1);
        for (int i = 0; i <= thetaSegments; i++) {
            double theta = thetaMin + (thetaMax - thetaMin) * i / thetaSegments;
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
     * Samples a full torus patch (full revolution around both axes).
     *
     * @param thetaSegments number of segments around the major axis
     * @param phiSegments number of segments around the tube cross-section
     * @return grid of sampled points (full torus)
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int thetaSegments, int phiSegments) {
        return sampleGrid(thetaSegments, phiSegments, 0.0, 2.0 * Math.PI, 0.0, 2.0 * Math.PI);
    }

    /**
     * Computes the normal at a given position on the torus surface.
     * The normal points outward from the tube center.
     *
     * @param theta angle around the major axis in radians
     * @param phi angle around the tube cross-section in radians
     * @return unit normal vector (outward from tube surface)
     */
    public Vector3 normalAt(double theta, double phi) {
        Preconditions.requireFinite(theta, "theta");
        Preconditions.requireFinite(phi, "phi");
        double nx = Math.cos(phi) * Math.cos(theta);
        double ny = Math.cos(phi) * Math.sin(theta);
        double nz = Math.sin(phi);
        Vector3 localNormal = new Vector3(nx, ny, nz);
        Vector3 worldNormal = position.xDirection().asVector().scale(localNormal.x())
                .add(position.yDirection().asVector().scale(localNormal.y()))
                .add(position.axis().asVector().scale(localNormal.z()));
        return worldNormal.normalize().asVector();
    }

    /**
     * Returns the bounding box for the full torus.
     *
     * @return bounding box enclosing the entire torus
     */
    public BoundingBox3 boundingBox() {
        CartesianPoint center = position.location();
        // Torus extends from (majorRadius - minorRadius) to (majorRadius + minorRadius) in radial directions
        // and from -minorRadius to +minorRadius in axial direction
        double radialExtent = majorRadius + minorRadius;
        CartesianPoint min = new CartesianPoint(
            center.x() - radialExtent,
            center.y() - radialExtent,
            center.z() - minorRadius
        );
        CartesianPoint max = new CartesianPoint(
            center.x() + radialExtent,
            center.y() + radialExtent,
            center.z() + minorRadius
        );
        return BoundingBox3.of(min, max);
    }

    /**
     * Returns the bounding box for a partial torus patch.
     *
     * @param thetaMin minimum theta angle
     * @param thetaMax maximum theta angle
     * @param phiMin minimum phi angle
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
     * Returns an approximate closest point on the torus surface to a given point.
     * Uses iterative approach to find the closest point.
     *
     * @param point target point
     * @return approximate closest point on the torus surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Transform point to torus local coordinates
        Vector3 offset = point.subtract(position.location());
        double localX = offset.dot(position.xDirection().asVector());
        double localY = offset.dot(position.yDirection().asVector());
        double localZ = offset.dot(position.axis().asVector());

        // Find theta (angle around major axis)
        double theta = Math.atan2(localY, localX);

        // Find radial distance from major axis
        double radialDist = Math.sqrt(localX * localX + localY * localY);

        // Find phi (angle around tube cross-section)
        // The tube center at this theta is at distance majorRadius from axis
        double tubeX = radialDist - majorRadius;
        double tubeY = localZ;
        double phi = Math.atan2(tubeY, tubeX);

        return pointAt(theta, phi);
    }

    /**
     * Returns an approximate shortest distance from a point to the torus surface.
     *
     * @param point target point
     * @return approximate shortest distance to the torus surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }
}
