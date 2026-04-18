package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal single-sheet hyperboloid surface (rotationally symmetric).
 * Parametrized as x^2/a^2 + y^2/a^2 - z^2/b^2 = 1 in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param radius radius at z=0 (waist)
 * @param semiAxis b parameter controlling the z-spread rate
 */
public record HyperboloidSurface(Axis2Placement3D position, double radius, double semiAxis) implements SurfaceGeometry {

    public HyperboloidSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        Preconditions.requireFinite(semiAxis, "semiAxis");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("radius must be greater than epsilon");
        }
        if (semiAxis <= Epsilon.EPS) {
            throw new GeometryException("semiAxis must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the hyperboloid.
     *
     * @param angle azimuthal angle in radians
     * @param z height parameter
     * @return point on the surface
     */
    public CartesianPoint pointAt(double angle, double z) {
        Preconditions.requireFinite(angle, "angle");
        Preconditions.requireFinite(z, "z");
        double factor = Math.sqrt(1.0 + z * z / (semiAxis * semiAxis));
        double r = radius * factor;
        double x = r * Math.cos(angle);
        double y = r * Math.sin(angle);
        Vector3 local = new Vector3(x, y, z);
        Vector3 world = position.xDirection().asVector().scale(local.x())
                .add(position.yDirection().asVector().scale(local.y()))
                .add(position.axis().asVector().scale(local.z()));
        return position.location().add(world);
    }

    /**
     * Samples the hyperboloid surface.
     *
     * @param angleSegments segments around the axis
     * @param zSegments segments along the height
     * @param zMax maximum height parameter
     * @return grid of sampled points
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(
            int angleSegments, int zSegments, double zMax) {
        double effectiveZMax = Math.max(1.0, zMax);
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(angleSegments + 1);
        for (int i = 0; i <= angleSegments; i++) {
            double angle = 2.0 * Math.PI * i / angleSegments;
            java.util.List<CartesianPoint> column = new java.util.ArrayList<>(zSegments + 1);
            for (int j = 0; j <= zSegments; j++) {
                double z = -effectiveZMax + 2.0 * effectiveZMax * j / zSegments;
                column.add(pointAt(angle, z));
            }
            grid.add(java.util.List.copyOf(column));
        }
        return java.util.List.copyOf(grid);
    }

    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        return sampleGrid(uSegments, vSegments, 1.0);
    }

    @Override
    public Vector3 normalAt(double angle, double z) {
        Preconditions.requireFinite(angle, "angle");
        Preconditions.requireFinite(z, "z");
        // Surface: F(x,y,z) = x^2 + y^2 - r0^2 * (1 + z^2/b^2) = 0
        double factor = Math.sqrt(1.0 + z * z / (semiAxis * semiAxis));
        double r = radius * factor;
        // Gradient: (2x, 2y, -2*r0^2*z/b^2)
        double nx = 2.0 * r * Math.cos(angle);
        double ny = 2.0 * r * Math.sin(angle);
        double nz = -2.0 * radius * radius * z / (semiAxis * semiAxis);
        Vector3 localNormal = new Vector3(nx, ny, nz);
        Vector3 worldNormal = position.xDirection().asVector().scale(localNormal.x())
                .add(position.yDirection().asVector().scale(localNormal.y()))
                .add(position.axis().asVector().scale(localNormal.z()));
        if (worldNormal.normSquared() <= Epsilon.EPS) {
            return position.xDirection().asVector();
        }
        return worldNormal.normalize().asVector();
    }

    @Override
    public BoundingBox3 boundingBox() {
        return SurfaceGeometry.super.boundingBox();
    }

    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(position.location());
        double x = offset.dot(position.xDirection().asVector());
        double y = offset.dot(position.yDirection().asVector());
        double z = offset.dot(position.axis().asVector());

        double bestAngle = 0;
        double bestZ = 0;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < 64; i++) {
            double angle = 2.0 * Math.PI * i / 64;
            for (int j = 0; j <= 32; j++) {
                double zc = -2.0 + 4.0 * j / 32;
                double factor = Math.sqrt(1.0 + zc * zc / (semiAxis * semiAxis));
                double r = radius * factor;
                double px = r * Math.cos(angle) - x;
                double py = r * Math.sin(angle) - y;
                double pz = zc - z;
                double d = px * px + py * py + pz * pz;
                if (d < minDist) {
                    minDist = d;
                    bestAngle = angle;
                    bestZ = zc;
                }
            }
        }
        return pointAt(bestAngle, bestZ);
    }

    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }
}
