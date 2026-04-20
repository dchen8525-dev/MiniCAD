package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
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
