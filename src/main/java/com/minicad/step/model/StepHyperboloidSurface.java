package com.minicad.step.model;

/**
 * Resolved HYPERBOLOID_SURFACE.
 * A quadric surface defined by a hyperboloid shape (one-sheet or two-sheet).
 */
public record StepHyperboloidSurface(
    int id,
    String name,
    StepEntity position,
    Double radius,
    Double semiAxis
) implements StepEntity {}
