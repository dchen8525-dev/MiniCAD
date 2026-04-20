package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal product definition.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param description optional description
 * @param formation referenced formation
 * @param frameOfReference referenced definition context
 */
public record StepProductDefinition(
        int id,
        String identifier,
        String description,
        StepProductDefinitionFormation formation,
        StepProductDefinitionContext frameOfReference
) implements StepEntity {

    @Override
    public String name() {
        return identifier;
    }
}
