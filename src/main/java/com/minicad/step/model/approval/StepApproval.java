package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal APPROVAL metadata.
 *
 * @param id STEP instance id
 * @param status approval status
 * @param level approval level
 */
public record StepApproval(
        int id,
        StepApprovalStatus status,
        String level
) implements StepEntity {

    @Override
    public String name() {
        return level;
    }
}
