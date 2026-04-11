package com.minicad.step.model;

/**
 * Minimal APPROVAL_STATUS metadata.
 *
 * @param id STEP instance id
 * @param name status label
 */
public record StepApprovalStatus(
        int id,
        String name
) implements StepEntity {
}
