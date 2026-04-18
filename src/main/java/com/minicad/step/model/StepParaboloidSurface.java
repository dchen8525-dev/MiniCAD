package com.minicad.step.model;

/**
 * Resolved PARABOLOID_SURFACE.
 * A quadric surface defined by a paraboloid shape.
 */
public record StepParaboloidSurface(
    int id,
    String name,
    StepEntity position,
    Double focalLength
) implements StepEntity {}
