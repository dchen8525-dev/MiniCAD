package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
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
