package com.minicad.step.model;

/**
 * Minimal REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped representation
 */
public record StepRepresentationMap(
        int id,
        StepEntity mappedOrigin,
        StepRepresentation mappedRepresentation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
