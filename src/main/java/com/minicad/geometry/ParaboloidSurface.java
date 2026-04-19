package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal paraboloid surface (rotationally symmetric).
 * Parametrized as z = (x^2 + y^2) / (4*f) in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param focalLength focal distance, must be positive
 */
public record ParaboloidSurface(Axis2Placement3D position, double focalLength) implements SurfaceGeometry {

    public ParaboloidSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(focalLength, "focalLength");
        if (focalLength <= Epsilon.EPS) {
            throw new GeometryException("focalLength must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the paraboloid.
     *
     * @param angle azimuthal angle in radians
     * @param v height parameter (z >= 0 for standard paraboloid)
     * @return point on the surface
     */
    public CartesianPoint pointAt(double angle, double v) {
        Preconditions.requireFinite(angle, "angle");
        Preconditions.requireFinite(v, "v");
        double r = Math.sqrt(4.0 * focalLength * Math.max(0.0, v));
        double x = r * Math.cos(angle);
        double y = r * Math.sin(angle);
        double z = v;
        Vector3 local = new Vector3(x, y, z);
        Vector3 world = position.xDirection().asVector().scale(local.x())
                .add(position.yDirection().asVector().scale(local.y()))
                .add(position.axis().asVector().scale(local.z()));
        return position.location().add(world);
    }

    /**
     * Samples the paraboloid surface.
     *
     * @param angleSegments segments around the axis
     * @param vSegments segments along the height
     * @param vMax maximum height parameter
     * @return grid of sampled points
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(
            int angleSegments, int vSegments, double vMax) {
        double effectiveVMax = Math.max(1.0, vMax);
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(angleSegments + 1);
        for (int i = 0; i <= angleSegments; i++) {
            double angle = 2.0 * Math.PI * i / angleSegments;
            java.util.List<CartesianPoint> column = new java.util.ArrayList<>(vSegments + 1);
            for (int j = 0; j <= vSegments; j++) {
                double v = effectiveVMax * j / vSegments;
                column.add(pointAt(angle, v));
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
    public Vector3 normalAt(double angle, double v) {
        Preconditions.requireFinite(angle, "angle");
        Preconditions.requireFinite(v, "v");
        // Surface: F(x,y,z) = x^2 + y^2 - 4*f*z = 0
        // Gradient: (2x, 2y, -4f) in local coords
        double r = Math.sqrt(4.0 * focalLength * Math.max(0.0, v));
        double nx = 2.0 * r * Math.cos(angle);
        double ny = 2.0 * r * Math.sin(angle);
        double nz = -4.0 * focalLength;
        Vector3 localNormal = new Vector3(nx, ny, nz);
        Vector3 worldNormal = position.xDirection().asVector().scale(localNormal.x())
                .add(position.yDirection().asVector().scale(localNormal.y()))
                .add(position.axis().asVector().scale(localNormal.z()));
        if (worldNormal.normSquared() <= Epsilon.EPS) {
            return position.axis().asVector();
        }
        return worldNormal.normalize().asVector();
    }

    @Override
    public BoundingBox3 boundingBox() {
        // Default parameter range: z in [0, 1], r = sqrt(4*f*v)
        double vMax = 1.0;
        double rMax = Math.sqrt(4.0 * focalLength * vMax);
        return analyticalBoundingBox(rMax, vMax);
    }

    /**
     * Returns the bounding box for a given parameter range.
     *
     * @param vMax maximum height parameter
     * @return bounding box enclosing the surface patch
     */
    public BoundingBox3 boundingBox(double vMax) {
        double effectiveVMax = Math.max(0.0, vMax);
        double rMax = Math.sqrt(4.0 * focalLength * effectiveVMax);
        return analyticalBoundingBox(rMax, effectiveVMax);
    }

    private BoundingBox3 analyticalBoundingBox(double rMax, double vMax) {
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        Vector3 zDir = position.axis().asVector();
        CartesianPoint center = position.location();

        // Surface extends: radius [0, rMax] in xy-plane, z in [0, vMax]
        // Max extent in each axis = rMax * |direction_component|
        double extentX = rMax * (Math.abs(xDir.x()) + Math.abs(yDir.x()));
        double extentY = rMax * (Math.abs(xDir.y()) + Math.abs(yDir.y()));
        double extentZ = Math.max(rMax * (Math.abs(xDir.z()) + Math.abs(yDir.z())),
                                   vMax * Math.abs(zDir.z()));

        // Also account for z-direction contribution
        double zExtentX = vMax * Math.abs(zDir.x());
        double zExtentY = vMax * Math.abs(zDir.y());
        double zExtentZ = vMax * Math.abs(zDir.z());

        return BoundingBox3.of(
            new CartesianPoint(center.x() - extentX - zExtentX, center.y() - extentY - zExtentY, center.z() - extentZ - zExtentZ),
            new CartesianPoint(center.x() + extentX + zExtentX, center.y() + extentY + zExtentY, center.z() + extentZ + zExtentZ)
        );
    }

    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Convert point to local coordinates
        Vector3 offset = point.subtract(position.location());
        double x = offset.dot(position.xDirection().asVector());
        double y = offset.dot(position.yDirection().asVector());
        double z = offset.dot(position.axis().asVector());

        // Find closest point on paraboloid z = (x^2 + y^2) / (4f)
        // Use sampling-based approach
        double bestAngle = 0;
        double bestV = 0;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < 64; i++) {
            double angle = 2.0 * Math.PI * i / 64;
            for (int j = 0; j <= 32; j++) {
                double v = 2.0 * Math.max(0, z) * j / 32;
                double r = Math.sqrt(4.0 * focalLength * v);
                double px = r * Math.cos(angle) - x;
                double py = r * Math.sin(angle) - y;
                double pz = v - z;
                double d = px * px + py * py + pz * pz;
                if (d < minDist) {
                    minDist = d;
                    bestAngle = angle;
                    bestV = v;
                }
            }
        }
        return pointAt(bestAngle, bestV);
    }

    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }
}
