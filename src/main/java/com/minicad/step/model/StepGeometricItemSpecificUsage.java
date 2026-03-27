package com.minicad.step.model;

/**
 * Minimal semantic PMI link from a callout to a geometric item.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param usage source PMI item
 * @param identifiedItem referenced geometric item
 */
public record StepGeometricItemSpecificUsage(
        int id,
        String name,
        String description,
        StepEntity usage,
        StepEntity identifiedItem
) implements StepEntity {
}
