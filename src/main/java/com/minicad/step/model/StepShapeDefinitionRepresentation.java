package com.minicad.step.model;

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
