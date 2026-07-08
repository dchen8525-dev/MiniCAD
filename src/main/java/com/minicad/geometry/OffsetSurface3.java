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
