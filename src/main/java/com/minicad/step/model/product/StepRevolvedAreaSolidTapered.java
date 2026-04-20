package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepAxis1Placement;
/**
 * Resolved REVOLVED_AREA_SOLID_TAPERED.
 * A revolved solid with tapered profile.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to revolve
 * @param axis axis of revolution
 * @param angle revolution angle
 * @param taperAngle taper angle
 */
public record StepRevolvedAreaSolidTapered(
    int id,
    String name,
    StepEntity sweptArea,
    StepAxis1Placement axis,
    double angle,
    double taperAngle) implements StepEntity {
}
