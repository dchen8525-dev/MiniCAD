package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal APPROVAL_DATE_TIME assignment.
 *
 * @param id STEP instance id
 * @param dateTime approval date and time
 * @param datedApproval approval being dated
 */
public final class StepApprovalDateTime extends AbstractStepEntity {
    private final StepDateAndTime dateTime;
    private final StepApproval datedApproval;

    public StepApprovalDateTime(int id, StepDateAndTime dateTime, StepApproval datedApproval) {
        super(id, "");
        this.dateTime = dateTime;
        this.datedApproval = datedApproval;
    }

    public StepDateAndTime getDateTime() {
        return dateTime;
    }

    public StepApproval getDatedApproval() {
        return datedApproval;
    }

    // Record-style accessors
    public StepDateAndTime dateTime() {
        return dateTime;
    }

    public StepApproval datedApproval() {
        return datedApproval;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "dateTime", dateTime, "datedApproval", datedApproval);
    }
}
