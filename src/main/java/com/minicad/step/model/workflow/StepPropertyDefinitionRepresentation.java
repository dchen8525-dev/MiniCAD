package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal property definition representation link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation property representation
 */
public record StepPropertyDefinitionRepresentation(
        int id,
        StepPropertyDefinition definition,
        StepRepresentation usedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
