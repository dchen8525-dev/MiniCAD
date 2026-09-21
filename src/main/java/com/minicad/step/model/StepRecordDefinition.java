package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RECORD_DEFINITION.
 * A record definition entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param recordType record variance type
 * @param recordFields record variance field definitions
 * @param recordKey record variance key fields
 * @param recordStatus record variance status
 */
public final class StepRecordDefinition extends AbstractStepEntity {
    private final String recordType;
    private final List<String> recordFields;
    private final List<String> recordKey;
    private final String recordStatus;

    public StepRecordDefinition(int id, String name, String recordType, List<String> recordFields, List<String> recordKey, String recordStatus) {
        super(id, name);
        this.recordType = recordType;
        this.recordFields = recordFields == null ? null : java.util.List.copyOf(recordFields);
        this.recordKey = recordKey == null ? null : java.util.List.copyOf(recordKey);
        this.recordStatus = recordStatus;
    }

    public String getRecordType() {
        return recordType;
    }

    public List<String> getRecordFields() {
        return recordFields;
    }

    public List<String> getRecordKey() {
        return recordKey;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("recordType", recordType);
        state.put("recordFields", recordFields);
        state.put("recordKey", recordKey);
        state.put("recordStatus", recordStatus);
        return state;
    }
}
