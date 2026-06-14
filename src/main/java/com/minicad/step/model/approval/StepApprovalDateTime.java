package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.date_time.StepDateAndTime;
import java.util.Objects;
/**
 * Minimal APPROVAL_DATE_TIME assignment.
 *
 * @param id STEP instance id
 * @param dateTime approval date and time
 * @param datedApproval approval being dated
 */
/**
 * Minimal APPROVAL_DATE_TIME assignment.
 *
 * @param id STEP instance id
 * @param dateTime approval date and time
 * @param datedApproval approval being dated
 */
public final class StepApprovalDateTime implements StepEntity {
    private final int id;
    private final StepDateAndTime dateTime;
    private final StepApproval datedApproval;

    public StepApprovalDateTime(int id, StepDateAndTime dateTime, StepApproval datedApproval) {
        this.id = id;
        this.dateTime = dateTime;
        this.datedApproval = datedApproval;
    }

    public int getId() {
        return id;
    }

    public StepDateAndTime getDateTime() {
        return dateTime;
    }

    public StepApproval getDatedApproval() {
        return datedApproval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApprovalDateTime that = (StepApprovalDateTime) o;
        return id == that.id && Objects.equals(dateTime, that.dateTime) && Objects.equals(datedApproval, that.datedApproval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, datedApproval);
    }

    @Override
    public String toString() {
        return "StepApprovalDateTime{" + "id=" + id + "dateTime=" + dateTime + "datedApproval=" + datedApproval + "}";
    }
}
