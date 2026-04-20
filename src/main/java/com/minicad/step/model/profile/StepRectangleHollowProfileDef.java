package com.minicad.step.model.profile;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepAxis2Placement2D;
/**
 * Resolved RECTANGLE_HOLLOW_PROFILE_DEF.
 * A rectangular hollow cross-section profile.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param xDim outer width
 * @param yDim outer height
 * @param wallThickness wall thickness
 * @param innerRadius inner corner radius (0 if sharp)
 */
public record StepRectangleHollowProfileDef(
    int id,
    String name,
    StepAxis2Placement2D position,
    double xDim,
    double yDim,
    double wallThickness,
    double innerRadius) implements StepEntity {
}
