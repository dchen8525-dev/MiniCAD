package com.minicad.step.model;

/**
 * Minimal next assembly usage occurrence.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param name occurrence name
 * @param description optional description
 * @param relatingProductDefinition assembly product definition
 * @param relatedProductDefinition component product definition
 */
public record StepNextAssemblyUsageOccurrence(
        int id,
        String identifier,
        String name,
        String description,
        StepProductDefinition relatingProductDefinition,
        StepProductDefinition relatedProductDefinition
) implements StepEntity {
}
