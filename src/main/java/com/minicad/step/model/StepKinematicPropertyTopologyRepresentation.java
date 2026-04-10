package com.minicad.step.model;

/**
 * Minimal kinematic property topology representation link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
public record StepKinematicPropertyTopologyRepresentation(
        int id,
        StepPropertyDefinition definition,
        StepRepresentation usedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
