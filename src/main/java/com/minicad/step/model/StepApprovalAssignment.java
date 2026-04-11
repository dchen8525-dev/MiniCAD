package com.minicad.step.model;

/**
 * Minimal APPROVAL_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedApproval assigned approval
 */
public record StepApprovalAssignment(
        int id,
        StepApproval assignedApproval
) implements StepEntity {

    @Override
    public String name() {
        return assignedApproval.name();
    }
}
