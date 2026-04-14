package com.minicad.step.model;

/**
 * Resolved CENTRE_LINE_ARC_PROFILE_DEF.
 * An arc profile defined along its centre line.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param radius arc radius
 * @param angle sweep angle
 */
public record StepCentreLineArcProfileDef(
    int id,
    String name,
    StepAxis2Placement2D position,
    double radius,
    double angle) implements StepEntity {
}
