package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS.
 * A toroidal surface where the axis is defined by a cylindrical axis placement.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 */
public record StepToroidalSurfaceWithCylindricalAxis(
    int id,
    String name,
    StepAxis1Placement position,
    double majorRadius,
    double minorRadius) implements StepEntity {
}