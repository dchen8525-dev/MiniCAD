package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepEscalationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceIssue;
    private final int varianceFrom;
    private final int varianceTo;
    private final String varianceReason;
    private final StepEntity varianceDate;
    private final StepEntity varianceHandler;
    private final String varianceStatus;

    public StepEscalationRecord(int id, String name, StepEntity varianceIssue, int varianceFrom, int varianceTo, String varianceReason, StepEntity varianceDate, StepEntity varianceHandler, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceIssue = varianceIssue;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceReason = varianceReason;
        this.varianceDate = varianceDate;
        this.varianceHandler = varianceHandler;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEscalationRecord that = (StepEscalationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceIssue, that.varianceIssue) && varianceFrom == that.varianceFrom && varianceTo == that.varianceTo && Objects.equals(varianceReason, that.varianceReason) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceHandler, that.varianceHandler) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceIssue, varianceFrom, varianceTo, varianceReason, varianceDate, varianceHandler, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepEscalationRecord{" + "id=" + id + "name=" + name + "varianceIssue=" + varianceIssue + "varianceFrom=" + varianceFrom + "varianceTo=" + varianceTo + "varianceReason=" + varianceReason + "varianceDate=" + varianceDate + "varianceHandler=" + varianceHandler + "varianceStatus=" + varianceStatus + "}";
    }
}