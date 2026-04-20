package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A conical surface with an elliptical cross-section.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param semiAngle semi-angle of the cone
 * @param semiAxisA first semi-axis of the ellipse at base
 * @param semiAxisB second semi-axis of the ellipse at base
 */
public record StepConicalSurfaceWithEllipticalAxis(
    int id,
    String name,
    StepAxis2Placement3D position,
    double semiAngle,
    double semiAxisA,
    double semiAxisB) implements StepEntity {
}