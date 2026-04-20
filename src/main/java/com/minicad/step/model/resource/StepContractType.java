package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal CONTRACT_TYPE metadata.
 *
 * @param id STEP instance id
 * @param description type description
 */
public record StepContractType(
        int id,
        String description
) implements StepEntity {

    @Override
    public String name() {
        return description;
    }
}
