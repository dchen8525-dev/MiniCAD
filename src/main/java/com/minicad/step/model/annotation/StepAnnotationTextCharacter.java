package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.product.StepRepresentationMap;
/**
 * Minimal ANNOTATION_TEXT_CHARACTER.
 *
 * @param id STEP instance id
 * @param name character name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
public record StepAnnotationTextCharacter(
        int id,
        String name,
        StepRepresentationMap mappingSource,
        StepEntity mappingTarget
) implements StepEntity {
}
