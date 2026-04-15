package com.minicad.step.model;

/**
 * Resolved CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A cylindrical surface with an elliptical cross-section.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param semiAxisA first semi-axis of the ellipse
 * @param semiAxisB second semi-axis of the ellipse
 */
public record StepCylindricalSurfaceWithEllipticalAxis(
    int id,
    String name,
    StepAxis2Placement3D position,
    double semiAxisA,
    double semiAxisB) implements StepEntity {
}