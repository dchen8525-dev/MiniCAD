package com.minicad.step.model;

/**
 * Resolved HYPERBOLA 2D.
 *
 * @param id step id
 * @param name step label
 * @param position placement of the hyperbola
 * @param semiAxis1 radius of the major axis
 * @param semiAxis2 radius of the minor axis
 */
public record StepHyperbola2D(
        int id,
        String name,
        StepAxis2Placement2D position,
        double semiAxis1,
        double semiAxis2
) implements StepEntity {
}
