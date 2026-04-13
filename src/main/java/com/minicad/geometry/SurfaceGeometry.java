package com.minicad.geometry;

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
        SurfaceOfRevolution3 {
}
