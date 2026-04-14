package com.minicad.step.model;

/**
 * Resolved CONFIGURATION_EFFECTIVITY.
 * Specifies when a configuration-managed item becomes effective.
 *
 * @param id STEP instance id
 * @param name effectivity name
 * @param configuration configuration item reference
 * @param itemConceived product definition being configured
 */
public record StepConfigurationEffectivity(
    int id,
    String name,
    StepEntity configuration,
    StepEntity itemConceived) implements StepEntity {
}
