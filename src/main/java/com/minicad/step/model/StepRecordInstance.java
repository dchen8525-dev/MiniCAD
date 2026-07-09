package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepRecordInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity recordDefinition;
    private final List<String> recordValues;
    private final StepEntity recordTimestamp;
    private final String recordStatus;

    public StepRecordInstance(int id, String name, StepEntity recordDefinition, List<String> recordValues, StepEntity recordTimestamp, String recordStatus) {
        this.id = id;
        this.name = name;
        this.recordDefinition = recordDefinition;
        this.recordValues = recordValues == null ? null : java.util.List.copyOf(recordValues);
        this.recordTimestamp = recordTimestamp;
        this.recordStatus = recordStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRecordInstance that = (StepRecordInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(recordDefinition, that.recordDefinition) && Objects.equals(recordValues, that.recordValues) && Objects.equals(recordTimestamp, that.recordTimestamp) && Objects.equals(recordStatus, that.recordStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recordDefinition, recordValues, recordTimestamp, recordStatus);
    }

    @Override
    public String toString() {
        return "StepRecordInstance{" + "id=" + id + "name=" + name + "recordDefinition=" + recordDefinition + "recordValues=" + recordValues + "recordTimestamp=" + recordTimestamp + "recordStatus=" + recordStatus + "}";
    }
}