package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
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
