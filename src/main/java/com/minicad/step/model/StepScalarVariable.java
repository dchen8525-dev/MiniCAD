package com.minicad.step.model;

/**
 * Minimal scalar variable link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
public record StepScalarVariable(
        int id,
        StepPropertyDefinition definition,
        StepRepresentation usedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
