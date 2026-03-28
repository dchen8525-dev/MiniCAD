package com.minicad.step.model;

/**
 * Minimal property definition metadata.
 *
 * @param id STEP instance id
 * @param name property name
 * @param description property description
 * @param definition related semantic target
 */
public record StepPropertyDefinition(
        int id,
        String name,
        String description,
        StepEntity definition
) implements StepEntity {
}
