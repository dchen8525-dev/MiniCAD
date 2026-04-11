package com.minicad.step.model;

/**
 * Minimal EFFECTIVITY metadata.
 *
 * @param id STEP instance id
 * @param effectivityId effectivity identifier
 */
public record StepEffectivity(
        int id,
        String effectivityId
) implements StepEntity {

    @Override
    public String name() {
        return effectivityId;
    }
}
