package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
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
