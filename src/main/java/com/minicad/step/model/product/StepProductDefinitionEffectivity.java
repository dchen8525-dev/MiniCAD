package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRODUCT_DEFINITION_EFFECTIVITY metadata.
 *
 * @param id STEP instance id
 * @param effectivityId effectivity identifier
 * @param usage usage text
 * @param productDefinition affected product definition
 */
public record StepProductDefinitionEffectivity(
        int id,
        String effectivityId,
        String usage,
        StepProductDefinition productDefinition
) implements StepEntity {

    @Override
    public String name() {
        return effectivityId;
    }
}
