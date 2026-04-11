package com.minicad.step.model;

/**
 * Minimal EFFECTIVITY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingEffectivity relating effectivity
 * @param relatedEffectivity related effectivity
 */
public record StepEffectivityRelationship(
        int id,
        String name,
        String description,
        StepEffectivity relatingEffectivity,
        StepEffectivity relatedEffectivity
) implements StepEntity {
}
