package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved HISTORY_RECORD.
 * A history record entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @param historyType history variance type
 * @param historyAction history variance action description
 * @param historyTarget history variance target reference
 * @param historyActor history variance actor reference
 * @param historyTimestamp history variance timestamp
 * @param historyStatus history variance status
 */
public final class StepHistoryRecord extends AbstractStepEntity {
    private final String historyType;
    private final String historyAction;
    private final StepEntity historyTarget;
    private final StepEntity historyActor;
    private final StepEntity historyTimestamp;
    private final String historyStatus;

    public StepHistoryRecord(int id, String name, String historyType, String historyAction, StepEntity historyTarget, StepEntity historyActor, StepEntity historyTimestamp, String historyStatus) {
        super(id, name);
        this.historyType = historyType;
        this.historyAction = historyAction;
        this.historyTarget = historyTarget;
        this.historyActor = historyActor;
        this.historyTimestamp = historyTimestamp;
        this.historyStatus = historyStatus;
    }

    public String getHistoryType() {
        return historyType;
    }

    public String getHistoryAction() {
        return historyAction;
    }

    public StepEntity getHistoryTarget() {
        return historyTarget;
    }

    public StepEntity getHistoryActor() {
        return historyActor;
    }

    public StepEntity getHistoryTimestamp() {
        return historyTimestamp;
    }

    public String getHistoryStatus() {
        return historyStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("historyType", historyType);
        state.put("historyAction", historyAction);
        state.put("historyTarget", historyTarget);
        state.put("historyActor", historyActor);
        state.put("historyTimestamp", historyTimestamp);
        state.put("historyStatus", historyStatus);
        return state;
    }
}
