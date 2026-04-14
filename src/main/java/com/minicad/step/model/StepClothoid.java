package com.minicad.step.model;

/**
 * Resolved CLOTHOID (Euler spiral / transition curve).
 *
 * @param id STEP instance id
 * @param name clothoid name
 * @param position placement defining the clothoid's local coordinate system
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter of the clothoid
 */
public record StepClothoid(
    int id,
    String name,
    StepEntity position,
    double xAxisIntercept,
    double curvature) implements StepEntity {
}
