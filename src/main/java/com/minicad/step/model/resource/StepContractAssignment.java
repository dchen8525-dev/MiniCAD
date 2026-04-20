package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal CONTRACT_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedContract assigned contract
 */
public record StepContractAssignment(
        int id,
        StepContract assignedContract
) implements StepEntity {

    @Override
    public String name() {
        return assignedContract.name();
    }
}
