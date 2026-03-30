package com.minicad.step.model;

/**
 * Resolved AXIS1_PLACEMENT.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param axis axis direction
 */
public record StepAxis1Placement(
        int id,
        String name,
        StepCartesianPoint location,
        StepDirection axis
) implements StepEntity {
}
