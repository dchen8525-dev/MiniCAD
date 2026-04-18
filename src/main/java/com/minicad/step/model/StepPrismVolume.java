package com.minicad.step.model;

/**
 * Resolved PRISM_VOLUME.
 * A CSG prism (wedge) primitive volume.
 */
public record StepPrismVolume(
    int id,
    String name,
    StepEntity position,
    Double width,
    Double depth,
    Double height
) implements StepEntity {}
