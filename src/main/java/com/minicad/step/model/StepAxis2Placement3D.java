package com.minicad.step.model;

/**
 * Resolved AXIS2_PLACEMENT_3D with explicit axis and ref direction.
 *
 * @param id step id
 * @param name step label
 * @param location origin point
 * @param axis local Z direction
 * @param refDirection local X direction
 */
public record StepAxis2Placement3D(
        int id,
        String name,
        StepCartesianPoint location,
        StepDirection axis,
        StepDirection refDirection
) implements StepEntity {
}
