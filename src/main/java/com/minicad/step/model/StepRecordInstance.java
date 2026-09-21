package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RECORD_INSTANCE.
 * A record instance entity.
 *
 * @param id STEP instance id
 * @param name record instance name
 * @param recordDefinition record variance definition reference
 * @param recordValues record variance field values
 * @param recordTimestamp record variance timestamp
 * @param recordStatus record variance status
 */
public final class StepRecordInstance extends AbstractStepEntity {
    private final StepEntity recordDefinition;
    private final List<String> recordValues;
    private final StepEntity recordTimestamp;
    private final String recordStatus;

    public StepRecordInstance(int id, String name, StepEntity recordDefinition, List<String> recordValues, StepEntity recordTimestamp, String recordStatus) {
        super(id, name);
        this.recordDefinition = recordDefinition;
        this.recordValues = recordValues == null ? null : java.util.List.copyOf(recordValues);
        this.recordTimestamp = recordTimestamp;
        this.recordStatus = recordStatus;
    }

    public StepEntity getRecordDefinition() {
        return recordDefinition;
    }

    public List<String> getRecordValues() {
        return recordValues;
    }

    public StepEntity getRecordTimestamp() {
        return recordTimestamp;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("recordDefinition", recordDefinition);
        state.put("recordValues", recordValues);
        state.put("recordTimestamp", recordTimestamp);
        state.put("recordStatus", recordStatus);
        return state;
    }
}
