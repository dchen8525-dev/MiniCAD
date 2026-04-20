package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
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
