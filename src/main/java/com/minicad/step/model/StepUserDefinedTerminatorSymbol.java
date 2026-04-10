package com.minicad.step.model;

/**
 * Minimal USER_DEFINED_TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
public record StepUserDefinedTerminatorSymbol(
        int id,
        String name,
        StepRepresentationMap mappingSource,
        StepEntity mappingTarget
) implements StepEntity {
}
