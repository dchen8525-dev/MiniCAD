package com.minicad.step.model;

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
