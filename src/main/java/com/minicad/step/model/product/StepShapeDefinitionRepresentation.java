package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
/**
 * Minimal shape definition representation link.
 *
 * @param id STEP instance id
 * @param definition referenced product definition shape
 * @param usedRepresentation referenced shape representation
 */
public record StepShapeDefinitionRepresentation(
        int id,
        StepProductDefinitionShape definition,
        StepRepresentation usedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
