package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
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
