package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal next assembly usage occurrence.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param name occurrence name
 * @param description optional description
 * @param relatingProductDefinition assembly product definition
 * @param relatedProductDefinition component product definition
 * @param referenceDesignator optional occurrence reference designator
 */
public record StepNextAssemblyUsageOccurrence(
        int id,
        String identifier,
        String name,
        String description,
        StepProductDefinition relatingProductDefinition,
        StepProductDefinition relatedProductDefinition,
        String referenceDesignator
) implements StepEntity {
}
