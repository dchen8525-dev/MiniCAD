package com.minicad.step.model;

/**
 * Resolved AXIS2_PLACEMENT_2D.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param refDirection local x direction
 */
public record StepAxis2Placement2D(
        int id,
        String name,
        StepCartesianPoint location,
        StepDirection refDirection
) implements StepEntity {
}
