package com.minicad.step.model;

/**
 * Resolved ELLIPSE.
 *
 * @param id step id
 * @param name step label
 * @param position ellipse placement
 * @param semiAxis1 local X semi-axis
 * @param semiAxis2 local Y semi-axis
 */
public record StepEllipse(
        int id,
        String name,
        StepAxis2Placement3D position,
        double semiAxis1,
        double semiAxis2
) implements StepEntity {
}
