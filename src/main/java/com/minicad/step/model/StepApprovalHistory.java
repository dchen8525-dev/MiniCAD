package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepApprovalHistory extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final List<StepEntity> varianceApprovals;
    private final String varianceCurrent;
    private final List<StepEntity> variancePending;
    private final String varianceStatus;

    public StepApprovalHistory(int id, String name, StepEntity varianceItem, List<StepEntity> varianceApprovals, String varianceCurrent, List<StepEntity> variancePending, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceApprovals = varianceApprovals == null ? null : java.util.List.copyOf(varianceApprovals);
        this.varianceCurrent = varianceCurrent;
        this.variancePending = variancePending == null ? null : java.util.List.copyOf(variancePending);
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceItem", varianceItem,
                "varianceApprovals", varianceApprovals, "varianceCurrent", varianceCurrent,
                "variancePending", variancePending, "varianceStatus", varianceStatus);
    }
}
