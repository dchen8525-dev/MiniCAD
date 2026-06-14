package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved APPROVAL_HISTORY.
 * An approval history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem approved variance item
 * @varianceApprovals approval variance entries
 * @varianceCurrent current variance approval status
 * @variancePending pending variance approvals
 * @varianceStatus history variance status
 */
/**
 * Resolved APPROVAL_HISTORY.
 * An approval history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem approved variance item
 * @varianceApprovals approval variance entries
 * @varianceCurrent current variance approval status
 * @variancePending pending variance approvals
 * @varianceStatus history variance status
 */
public final class StepApprovalHistory implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final List<StepEntity> varianceApprovals;
    private final String varianceCurrent;
    private final List<StepEntity> variancePending;
    private final String varianceStatus;

    public StepApprovalHistory(int id, String name, StepEntity varianceItem, List<StepEntity> varianceApprovals, String varianceCurrent, List<StepEntity> variancePending, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceApprovals = varianceApprovals == null ? null : java.util.List.copyOf(varianceApprovals);
        this.varianceCurrent = varianceCurrent;
        this.variancePending = variancePending == null ? null : java.util.List.copyOf(variancePending);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public List<StepEntity> getVarianceApprovals() {
        return varianceApprovals;
    }

    public String getVarianceCurrent() {
        return varianceCurrent;
    }

    public List<StepEntity> getVariancePending() {
        return variancePending;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepApprovalHistory that = (StepApprovalHistory) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceApprovals, that.varianceApprovals) && Objects.equals(varianceCurrent, that.varianceCurrent) && Objects.equals(variancePending, that.variancePending) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceApprovals, varianceCurrent, variancePending, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepApprovalHistory{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceApprovals=" + varianceApprovals + "varianceCurrent=" + varianceCurrent + "variancePending=" + variancePending + "varianceStatus=" + varianceStatus + "}";
    }
}