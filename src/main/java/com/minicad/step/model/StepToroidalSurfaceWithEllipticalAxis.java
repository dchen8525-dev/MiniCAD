package com.minicad.step.model;

/**
 * Resolved TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A toroidal surface where the axis is defined by an elliptical axis placement.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 * @param ellipticalRatio ratio defining the elliptical cross-section
 */
public record StepToroidalSurfaceWithEllipticalAxis(
    int id,
    String name,
    StepAxis2Placement3D position,
    double majorRadius,
    double minorRadius,
    double ellipticalRatio) implements StepEntity {
}