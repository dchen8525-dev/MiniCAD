package com.minicad.step.model;

/**
 * Minimal ANNOTATION_TEXT.
 *
 * @param id STEP instance id
 * @param name annotation name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
public record StepAnnotationText(
        int id,
        String name,
        StepRepresentationMap mappingSource,
        StepEntity mappingTarget
) implements StepEntity {
}
