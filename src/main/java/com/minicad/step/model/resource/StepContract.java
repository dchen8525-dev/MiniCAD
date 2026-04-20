package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal CONTRACT metadata.
 *
 * @param id STEP instance id
 * @param name contract name
 * @param purpose contract purpose
 * @param kind contract type
 */
public record StepContract(
        int id,
        String name,
        String purpose,
        StepContractType kind
) implements StepEntity {
}
