package com.minicad.step.model;

/**
 * Minimal action property representation link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
public record StepActionPropertyRepresentation(
        int id,
        StepPropertyDefinition definition,
        StepRepresentation usedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
