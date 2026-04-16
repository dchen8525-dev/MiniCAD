package com.minicad.step.model;

/**
 * Resolved CIRCLE_2D.
 * A circle in 2D parameter space.
 *
 * @param id step id
 * @param name step label
 * @param position 2D placement (center and direction)
 * @param radius circle radius
 */
public record StepCircle2D(
        int id,
        String name,
        StepAxis2Placement2D position,
        double radius
) implements StepEntity {
}
