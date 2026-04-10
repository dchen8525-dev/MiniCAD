package com.minicad.step.model;

/**
 * Minimal item-identified representation usage subtype carrying a placed target.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition usage definition/select target
 * @param usedRepresentation representation carrying the item
 * @param identifiedItem identified item reference
 */
public record StepPlacedTarget(
        int id,
        String name,
        String description,
        StepEntity definition,
        StepRepresentation usedRepresentation,
        StepEntity identifiedItem
) implements StepEntity {
}
