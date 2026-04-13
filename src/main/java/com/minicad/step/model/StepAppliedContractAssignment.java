package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_CONTRACT_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedContract assigned contract
 * @param items assigned target items
 */
public record StepAppliedContractAssignment(
        int id,
        String entityName,
        StepContract assignedContract,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedContractAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return assignedContract.name();
    }
}
