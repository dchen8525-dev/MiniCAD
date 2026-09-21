package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal APPROVAL_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedApproval assigned approval
 */
public final class StepApprovalAssignment extends AbstractStepEntity {
    private final StepApproval assignedApproval;

    public StepApprovalAssignment(int id, StepApproval assignedApproval) {
        super(id, "");
        this.assignedApproval = assignedApproval;
    }

    public StepApproval getAssignedApproval() {
        return assignedApproval;
    }

    // Record-style accessor
    public StepApproval assignedApproval() {
        return assignedApproval;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedApproval", assignedApproval);
        return state;
    }
}
