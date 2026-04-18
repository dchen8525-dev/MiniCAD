package com.minicad.step.model;

/**
 * Resolved SPHERE_VOLUME.
 * A CSG sphere primitive volume.
 */
public record StepSphereVolume(
    int id,
    String name,
    StepEntity center,
    Double radius
) implements StepEntity {}
