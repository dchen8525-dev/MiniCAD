package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CONTRACT_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedContract assigned contract
 */
public final class StepContractAssignment extends AbstractStepEntity {
    private final StepContract assignedContract;

    public StepContractAssignment(int id, StepContract assignedContract) {
        super(id, "");
        this.assignedContract = assignedContract;
    }

    public StepContract getAssignedContract() {
        return assignedContract;
    }

    // Record-style accessor
    public StepContract assignedContract() {
        return assignedContract;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedContract", assignedContract);
        return state;
    }
}
