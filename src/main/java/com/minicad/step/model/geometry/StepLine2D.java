package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved LINE_2D.
 *
 * @param id step id
 * @param name step label
 * @param point_2d point on the line
 * @param direction_2d direction of the line
 */
public record StepLine2D(
        int id,
        String name,
        StepCartesianPoint point2d,
        StepDirection direction2d
) implements StepEntity {
}
