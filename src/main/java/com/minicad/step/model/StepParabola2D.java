package com.minicad.step.model;

/**
 * Resolved PARABOLA 2D.
 *
 * @param id step id
 * @param name step label
 * @param position placement of the parabola
 * @param focalDist focal distance of the parabola
 */
public record StepParabola2D(
        int id,
        String name,
        StepAxis2Placement2D position,
        double focalDist
) implements StepEntity {
}
