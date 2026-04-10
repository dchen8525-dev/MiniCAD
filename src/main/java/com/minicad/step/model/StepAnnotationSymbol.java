package com.minicad.step.model;

/**
 * Minimal ANNOTATION_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param mappingSource symbol representation map
 * @param mappingTarget placement target
 */
public record StepAnnotationSymbol(
        int id,
        String name,
        StepSymbolRepresentationMap mappingSource,
        StepEntity mappingTarget
) implements StepEntity {
}
