package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A spherical surface with an elliptical axis definition.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param radius sphere radius
 * @param ellipticalRatio ratio defining the elliptical shape
 */
public record StepSphericalSurfaceWithEllipticalAxis(
    int id,
    String name,
    StepAxis2Placement3D position,
    double radius,
    double ellipticalRatio) implements StepEntity {
}