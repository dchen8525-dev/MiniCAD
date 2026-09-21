package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CHANGE_RECORD.
 * A change record entity.
 *
 * @param id STEP instance id
 * @param name change name
 * @param changeType change variance type
 * @param changeDescription change variance description
 * @param changeTarget change variance target reference
 * @param changeReason change variance reason
 * @param changeTimestamp change variance timestamp
 * @param changeStatus change variance status
 */
public final class StepChangeRecord extends AbstractStepEntity {
    private final String changeType;
    private final String changeDescription;
    private final StepEntity changeTarget;
    private final String changeReason;
    private final StepEntity changeTimestamp;
    private final String changeStatus;

    public StepChangeRecord(int id, String name, String changeType, String changeDescription, StepEntity changeTarget, String changeReason, StepEntity changeTimestamp, String changeStatus) {
        super(id, name);
        this.changeType = changeType;
        this.changeDescription = changeDescription;
        this.changeTarget = changeTarget;
        this.changeReason = changeReason;
        this.changeTimestamp = changeTimestamp;
        this.changeStatus = changeStatus;
    }

    public String getChangeType() {
        return changeType;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public StepEntity getChangeTarget() {
        return changeTarget;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public StepEntity getChangeTimestamp() {
        return changeTimestamp;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("changeType", changeType);
        state.put("changeDescription", changeDescription);
        state.put("changeTarget", changeTarget);
        state.put("changeReason", changeReason);
        state.put("changeTimestamp", changeTimestamp);
        state.put("changeStatus", changeStatus);
        return state;
    }
}
