package com.minicad.step.model;

/**
 * Resolved TORUS_VOLUME.
 * A CSG torus primitive volume.
 */
public record StepTorusVolume(
    int id,
    String name,
    StepEntity position,
    Double majorRadius,
    Double minorRadius
) implements StepEntity {}
