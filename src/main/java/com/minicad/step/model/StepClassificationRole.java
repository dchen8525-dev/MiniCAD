package com.minicad.step.model;

/**
 * Minimal CLASSIFICATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public record StepClassificationRole(
        int id,
        String name
) implements StepEntity {
}
