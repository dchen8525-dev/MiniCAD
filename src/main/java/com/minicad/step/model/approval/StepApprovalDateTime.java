package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.date_time.StepDateAndTime;
/**
 * Minimal APPROVAL_DATE_TIME assignment.
 *
 * @param id STEP instance id
 * @param dateTime approval date and time
 * @param datedApproval approval being dated
 */
public record StepApprovalDateTime(
        int id,
        StepDateAndTime dateTime,
        StepApproval datedApproval
) implements StepEntity {

    @Override
    public String name() {
        return dateTime.name();
    }
}
