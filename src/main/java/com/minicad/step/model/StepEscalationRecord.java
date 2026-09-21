package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ESCALATION_RECORD.
 * An escalation record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceIssue escalated variance issue
 * @varianceFrom escalation variance from level
 * @varianceTo escalation variance to level
 * @varianceReason escalation variance reason
 * @varianceDate escalation variance date
 * @varianceHandler handler variance reference
 * @varianceStatus record variance status
 */
public final class StepEscalationRecord extends AbstractStepEntity {
    private final StepEntity varianceIssue;
    private final int varianceFrom;
    private final int varianceTo;
    private final String varianceReason;
    private final StepEntity varianceDate;
    private final StepEntity varianceHandler;
    private final String varianceStatus;

    public StepEscalationRecord(int id, String name, StepEntity varianceIssue, int varianceFrom, int varianceTo, String varianceReason, StepEntity varianceDate, StepEntity varianceHandler, String varianceStatus) {
        super(id, name);
        this.varianceIssue = varianceIssue;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceReason = varianceReason;
        this.varianceDate = varianceDate;
        this.varianceHandler = varianceHandler;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceIssue() {
        return varianceIssue;
    }

    public int getVarianceFrom() {
        return varianceFrom;
    }

    public int getVarianceTo() {
        return varianceTo;
    }

    public String getVarianceReason() {
        return varianceReason;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceHandler() {
        return varianceHandler;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceIssue", varianceIssue);
        state.put("varianceFrom", varianceFrom);
        state.put("varianceTo", varianceTo);
        state.put("varianceReason", varianceReason);
        state.put("varianceDate", varianceDate);
        state.put("varianceHandler", varianceHandler);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
