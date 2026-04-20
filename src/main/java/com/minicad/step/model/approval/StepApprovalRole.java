package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
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
