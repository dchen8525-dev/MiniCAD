package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved EXTERNALLY_DEFINED_HATCH_STYLE.
 * A hatch style defined by an external source.
 */
public record StepExternallyDefinedHatchStyle(
    int id,
    String name,
    StepEntity externalSource) implements StepEntity {
}
