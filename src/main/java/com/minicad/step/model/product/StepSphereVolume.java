package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
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
