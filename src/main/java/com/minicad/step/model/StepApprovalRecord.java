package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepApprovalRecord extends AbstractStepEntity {
    private final String approvalId;
    private final StepEntity varianceItem;
    private final StepEntity varianceApprover;
    private final StepEntity varianceRole;
    private final StepEntity varianceDate;
    private final String varianceDecision;
    private final String varianceComments;
    private final String varianceStatus;

    public StepApprovalRecord(int id, String name, String approvalId, StepEntity varianceItem, StepEntity varianceApprover, StepEntity varianceRole, StepEntity varianceDate, String varianceDecision, String varianceComments, String varianceStatus) {
        super(id, name);
        this.approvalId = approvalId;
        this.varianceItem = varianceItem;
        this.varianceApprover = varianceApprover;
        this.varianceRole = varianceRole;
        this.varianceDate = varianceDate;
        this.varianceDecision = varianceDecision;
        this.varianceComments = varianceComments;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("approvalId", approvalId);
        state.put("varianceItem", varianceItem);
        state.put("varianceApprover", varianceApprover);
        state.put("varianceRole", varianceRole);
        state.put("varianceDate", varianceDate);
        state.put("varianceDecision", varianceDecision);
        state.put("varianceComments", varianceComments);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
