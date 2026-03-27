package com.minicad.step.model;

/**
 * Minimal product definition shape.
 *
 * @param id STEP instance id
 * @param name shape name
 * @param description optional description
 * @param definition referenced product definition
 */
public record StepProductDefinitionShape(
        int id,
        String name,
        String description,
        StepProductDefinition definition
) implements StepEntity {
}
