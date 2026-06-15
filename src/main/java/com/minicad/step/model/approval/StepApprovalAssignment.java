package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal APPROVAL_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedApproval assigned approval
 */
/**
 * Minimal APPROVAL_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedApproval assigned approval
 */
public final class StepApprovalAssignment implements StepEntity {
    private final int id;
    private final StepApproval assignedApproval;

    public StepApprovalAssignment(int id, StepApproval assignedApproval) {
        this.id = id;
        this.assignedApproval = assignedApproval;
    }

    public int getId() {
        return id;
    }

    public StepApproval getAssignedApproval() {
        return assignedApproval;
    }

    // Record-style accessor
    public StepApproval assignedApproval() {
        return assignedApproval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApprovalAssignment that = (StepApprovalAssignment) o;
        return id == that.id && Objects.equals(assignedApproval, that.assignedApproval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedApproval);
    }

    @Override
    public String toString() {
        return "StepApprovalAssignment{" + "id=" + id + "assignedApproval=" + assignedApproval + "}";
    }
}
