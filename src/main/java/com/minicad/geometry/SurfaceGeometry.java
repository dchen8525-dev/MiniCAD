package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Marker interface for supported face surface geometry.
 */
public sealed interface SurfaceGeometry permits
        Plane,
        OffsetSurface3,
        CylindricalSurface,
        ConicalSurface,
        SphericalSurface,
        ToroidalSurface,
        BSplineSurface3,
        RationalBSplineSurface3,
        SurfaceOfLinearExtrusion3,
        SurfaceOfRevolution3,
        RuledSurface3,
        SurfaceOfConstantRadius3,
        ParaboloidSurface,
        HyperboloidSurface,
        SurfaceOfTranslation3,
        SurfaceOfProjection3 {

    /**
     * Returns the approximate bounding box of the surface by sampling.
     * Implementations may override with more efficient calculations.
     *
     * @return bounding box enclosing the surface
     */
    default BoundingBox3 boundingBox() {
        java.util.List<java.util.List<CartesianPoint>> grid = sampleGrid(32, 32);
        if (grid.isEmpty()) {
            return BoundingBox3.empty();
        }
        BoundingBox3.Box box = BoundingBox3.mutable();
        for (java.util.List<CartesianPoint> row : grid) {
            for (CartesianPoint point : row) {
                box.expand(point);
            }
        }
        return box.toImmutable();
    }

    /**
     * Samples a grid of points on the surface.
     * Implementations should override with surface-specific sampling logic.
     *
     * @param uSegments number of segments along U direction
     * @param vSegments number of segments along V direction
     * @return grid of sampled points
     */
    default java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>();
        return java.util.List.copyOf(grid);
    }

    /**
     * Returns the surface normal at a point.
     * Default implementation computes using numerical partial derivatives.
     *
     * @param u parameter along U direction
     * @param v parameter along V direction
     * @return unit normal vector
     */
    default Vector3 normalAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        java.util.List<java.util.List<CartesianPoint>> grid = sampleGrid(64, 64);
        if (grid.isEmpty() || grid.get(0).isEmpty()) {
            return new Vector3(0, 0, 1);
        }
        int ui = (int) (u * (grid.size() - 1));
        int vi = (int) (v * (grid.get(0).size() - 1));
        ui = Math.max(0, Math.min(ui, grid.size() - 2));
        vi = Math.max(0, Math.min(vi, grid.get(0).size() - 2));

        CartesianPoint pu = grid.get(ui).get(vi);
        CartesianPoint pu2 = grid.get(ui + 1).get(vi);
        CartesianPoint pv = grid.get(ui).get(vi + 1);

        Vector3 tangentU = pu2.subtract(pu);
        Vector3 tangentV = pv.subtract(pu);
        Vector3 normal = tangentU.cross(tangentV);

        double norm = normal.norm();
        if (norm <= Epsilon.EPS) {
            return new Vector3(0, 0, 1);
        }
        return normal.scale(1.0 / norm);
    }
}
