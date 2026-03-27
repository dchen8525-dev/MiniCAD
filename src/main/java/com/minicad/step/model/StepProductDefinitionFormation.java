package com.minicad.step.model;

/**
 * Minimal product definition formation.
 *
 * @param id STEP instance id
 * @param name formation name
 * @param description optional description
 * @param ofProduct referenced product
 */
public record StepProductDefinitionFormation(
        int id,
        String name,
        String description,
        StepProduct ofProduct
) implements StepEntity {
}
