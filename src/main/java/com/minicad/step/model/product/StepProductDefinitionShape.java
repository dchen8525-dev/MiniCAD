package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal product definition shape.
 *
 * @param id STEP instance id
 * @param name shape name
 * @param description optional description
 * @param definition referenced product definition or product definition relationship
 */
public record StepProductDefinitionShape(
        int id,
        String name,
        String description,
        StepEntity definition
) implements StepEntity {
}
