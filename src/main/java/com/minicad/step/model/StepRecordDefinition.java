package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepRecordDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String recordType;
    private final List<String> recordFields;
    private final List<String> recordKey;
    private final String recordStatus;

    public StepRecordDefinition(int id, String name, String recordType, List<String> recordFields, List<String> recordKey, String recordStatus) {
        this.id = id;
        this.name = name;
        this.recordType = recordType;
        this.recordFields = recordFields == null ? null : java.util.List.copyOf(recordFields);
        this.recordKey = recordKey == null ? null : java.util.List.copyOf(recordKey);
        this.recordStatus = recordStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRecordDefinition that = (StepRecordDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(recordType, that.recordType) && Objects.equals(recordFields, that.recordFields) && Objects.equals(recordKey, that.recordKey) && Objects.equals(recordStatus, that.recordStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recordType, recordFields, recordKey, recordStatus);
    }

    @Override
    public String toString() {
        return "StepRecordDefinition{" + "id=" + id + "name=" + name + "recordType=" + recordType + "recordFields=" + recordFields + "recordKey=" + recordKey + "recordStatus=" + recordStatus + "}";
    }
}