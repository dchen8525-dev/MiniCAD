package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.product.StepProductDefinitionShape;
/**
 * Minimal shape aspect.
 *
 * @param id STEP instance id
 * @param name aspect name
 * @param description aspect description
 * @param ofShape owning product definition shape
 * @param productDefinitional STEP LOGICAL value as text
 * @param entityName concrete STEP entity name
 */
public record StepShapeAspect(
        int id,
        String name,
        String description,
        StepProductDefinitionShape ofShape,
        String productDefinitional,
        String entityName
) implements StepEntity {
}
