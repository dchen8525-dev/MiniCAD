package com.minicad.step.model;

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
