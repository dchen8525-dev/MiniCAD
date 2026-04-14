package com.minicad.step.model;

/**
 * Resolved EXTERNALLY_DEFINED_HATCH_STYLE.
 * A hatch style defined by an external source.
 *
 * @param id STEP instance id
 * @param name style name
 * @param item externally defined item
 * @param source external source
 */
public record StepExternallyDefinedHatchStyle(
    int id,
    String name,
    StepEntity item,
    StepEntity source) implements StepEntity {
}
