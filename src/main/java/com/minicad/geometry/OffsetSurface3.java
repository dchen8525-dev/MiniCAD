package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal offset surface wrapper around another supported surface geometry.
 * An offset surface is parallel to the basis surface at a constant distance.
 *
 * @param basisSurface wrapped basis surface
 * @param distance offset distance (positive offsets along normal direction)
 */
/**
 * Minimal offset surface wrapper around another supported surface geometry.
 * An offset surface is parallel to the basis surface at a constant distance.
 *
 * @param basisSurface wrapped basis surface
 * @param distance offset distance (positive offsets along normal direction)
 */
public final class OffsetSurface3 implements SurfaceGeometry {
    private final SurfaceGeometry basisSurface;
    private final double distance;

    public OffsetSurface3(SurfaceGeometry basisSurface, double distance) {
        this.basisSurface = basisSurface;
        this.distance = distance;
    }

    public SurfaceGeometry getBasisSurface() {
        return basisSurface;
    }

    public double getDistance() {
        return distance;
    }

    // Record-style accessors
    public SurfaceGeometry basisSurface() { return getBasisSurface(); }
    public double distance() { return getDistance(); }

    /**
     * {@inheritDoc}
     *
     * <p>{@code P(u, v) = basis(u, v) + distance · basisNormal(u, v)}. The
     * offset is signed: a negative distance offsets against the basis normal.</p>
     */
    @Override
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint base = basisSurface.pointAt(u, v);
        Vector3 normal = basisSurface.normalAt(u, v);
        return base.add(normal.scale(distance));
    }

    /**
     * {@inheritDoc}
     *
     * <p>A constant-distance offset shares its tangent planes with the basis
     * surface, so the normal is the basis normal. The two only diverge once the
     * offset is large enough to fold the surface back through a center of
     * curvature, which this kernel does not detect.</p>
     */
    @Override
    public Vector3 normalAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        return basisSurface.normalAt(u, v);
    }

    /**
     * Samples the offset of a uniform [0,1]² parameter grid over the basis
     * surface (the surface builders produce normalized-knot bases, whose
     * natural domain is [0,1]²).
     */
    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        int uCount = Math.max(uSegments, 1);
        int vCount = Math.max(vSegments, 1);
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>(uCount + 1);
        for (int iu = 0; iu <= uCount; iu++) {
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>(vCount + 1);
            for (int iv = 0; iv <= vCount; iv++) {
                row.add(pointAt((double) iu / uCount, (double) iv / vCount));
            }
            grid.add(java.util.List.copyOf(row));
        }
        return java.util.List.copyOf(grid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffsetSurface3 that = (OffsetSurface3) o;
        return Objects.equals(basisSurface, that.basisSurface) && distance == that.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(basisSurface, distance);
    }

    @Override
    public String toString() {
        return "OffsetSurface3{" + "basisSurface=" + basisSurface + "distance=" + distance + "}";
    }
}
