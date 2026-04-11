package com.minicad.step.model;

/**
 * Minimal SHAPE_ASPECT_OCCURRENCE metadata.
 *
 * @param id STEP instance id
 * @param name aspect name
 * @param description aspect description
 * @param ofShape owning product definition shape
 * @param productDefinitional LOGICAL value encoded as T, F or U
 * @param definition occurrence definition
 * @param entityName concrete STEP entity name
 */
public record StepShapeAspectOccurrence(
        int id,
        String name,
        String description,
        StepProductDefinitionShape ofShape,
        String productDefinitional,
        StepEntity definition,
        String entityName
) implements StepEntity {
}
