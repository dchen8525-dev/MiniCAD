package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved APPROVAL_RECORD.
 * An approval record entity.
 *
 * @param id STEP instance id
 * @param name approval name
 * @param approvalId approval identifier
 * @varianceItem approved variance item
 * @varianceApprover approving variance person
 * @varianceRole approver variance role
 * @varianceDate approval variance date
 * @varianceDecision approval variance decision
 * @varianceComments approval variance comments
 * @varianceStatus approval variance status
 */
/**
 * Resolved APPROVAL_RECORD.
 * An approval record entity.
 *
 * @param id STEP instance id
 * @param name approval name
 * @param approvalId approval identifier
 * @varianceItem approved variance item
 * @varianceApprover approving variance person
 * @varianceRole approver variance role
 * @varianceDate approval variance date
 * @varianceDecision approval variance decision
 * @varianceComments approval variance comments
 * @varianceStatus approval variance status
 */
public final class StepApprovalRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String approvalId;
    private final StepEntity varianceItem;
    private final StepEntity varianceApprover;
    private final StepEntity varianceRole;
    private final StepEntity varianceDate;
    private final String varianceDecision;
    private final String varianceComments;
    private final String varianceStatus;

    public StepApprovalRecord(int id, String name, String approvalId, StepEntity varianceItem, StepEntity varianceApprover, StepEntity varianceRole, StepEntity varianceDate, String varianceDecision, String varianceComments, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.approvalId = approvalId;
        this.varianceItem = varianceItem;
        this.varianceApprover = varianceApprover;
        this.varianceRole = varianceRole;
        this.varianceDate = varianceDate;
        this.varianceDecision = varianceDecision;
        this.varianceComments = varianceComments;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public StepEntity getVarianceApprover() {
        return varianceApprover;
    }

    public StepEntity getVarianceRole() {
        return varianceRole;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceDecision() {
        return varianceDecision;
    }

    public String getVarianceComments() {
        return varianceComments;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApprovalRecord that = (StepApprovalRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(approvalId, that.approvalId) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceApprover, that.varianceApprover) && Objects.equals(varianceRole, that.varianceRole) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceDecision, that.varianceDecision) && Objects.equals(varianceComments, that.varianceComments) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, approvalId, varianceItem, varianceApprover, varianceRole, varianceDate, varianceDecision, varianceComments, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepApprovalRecord{" + "id=" + id + "name=" + name + "approvalId=" + approvalId + "varianceItem=" + varianceItem + "varianceApprover=" + varianceApprover + "varianceRole=" + varianceRole + "varianceDate=" + varianceDate + "varianceDecision=" + varianceDecision + "varianceComments=" + varianceComments + "varianceStatus=" + varianceStatus + "}";
    }
}