package com.minicad.step.model;

/**
 * Resolved CENTERED_CIRCLE_PROFILE_DEF.
 * A circular profile with explicit center offset.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param radius circle radius
 * @param centerOffset center offset distance
 */
public record StepCenteredCircleProfileDef(
    int id,
    String name,
    StepAxis2Placement2D position,
    double radius,
    double centerOffset) implements StepEntity {
}
