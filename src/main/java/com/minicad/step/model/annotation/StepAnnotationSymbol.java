package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepSymbolRepresentationMap;
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
