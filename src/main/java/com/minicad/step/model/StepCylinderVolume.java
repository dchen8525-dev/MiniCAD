package com.minicad.step.model;

/**
 * Resolved CYLINDER_VOLUME.
 * A CSG cylinder primitive volume.
 */
public record StepCylinderVolume(
    int id,
    String name,
    StepEntity position,
    Double radius,
    Double height
) implements StepEntity {}
