package com.minicad.step.model;

/**
 * Minimal APPROVAL_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public record StepApprovalRole(
        int id,
        String name
) implements StepEntity {
}
