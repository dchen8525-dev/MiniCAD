package com.minicad.step.model;

/**
 * Resolved DESIGN_MAKE_FROM.
 * Design-to-manufacturing mapping.
 */
public record StepDesignMakeFrom(
    int id,
    String name,
    String description,
    StepEntity design,
    StepEntity manufacturing) implements StepEntity {
}
