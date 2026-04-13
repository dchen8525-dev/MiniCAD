package com.minicad.geometry;

import com.minicad.common.Preconditions;

/**
 * Minimal offset surface wrapper around another supported surface geometry.
 *
 * @param basisSurface wrapped basis surface
 * @param distance offset distance
 */
public record OffsetSurface3(SurfaceGeometry basisSurface, double distance) implements SurfaceGeometry {

    public OffsetSurface3 {
        Preconditions.requireNonNull(basisSurface, "basisSurface");
        Preconditions.requireFinite(distance, "distance");
    }
}
