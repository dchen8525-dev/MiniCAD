package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LOG_INSTANCE.
 * A log instance entity.
 *
 * @param id STEP instance id
 * @param name log instance name
 * @param logDefinition log variance definition reference
 * @param logEntries log variance entries
 * @param logSize log variance size
 * @param logStartTime log variance start time
 * @param logEndTime log variance end time
 * @param logStatus log variance status
 */
/**
 * Resolved LOG_INSTANCE.
 * A log instance entity.
 *
 * @param id STEP instance id
 * @param name log instance name
 * @param logDefinition log variance definition reference
 * @param logEntries log variance entries
 * @param logSize log variance size
 * @param logStartTime log variance start time
 * @param logEndTime log variance end time
 * @param logStatus log variance status
 */
public final class StepLogInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity logDefinition;
    private final List<String> logEntries;
    private final long logSize;
    private final StepEntity logStartTime;
    private final StepEntity logEndTime;
    private final String logStatus;

    public StepLogInstance(int id, String name, StepEntity logDefinition, List<String> logEntries, long logSize, StepEntity logStartTime, StepEntity logEndTime, String logStatus) {
        this.id = id;
        this.name = name;
        this.logDefinition = logDefinition;
        this.logEntries = logEntries == null ? null : java.util.List.copyOf(logEntries);
        this.logSize = logSize;
        this.logStartTime = logStartTime;
        this.logEndTime = logEndTime;
        this.logStatus = logStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getLogDefinition() {
        return logDefinition;
    }

    public List<String> getLogEntries() {
        return logEntries;
    }

    public long getLogSize() {
        return logSize;
    }

    public StepEntity getLogStartTime() {
        return logStartTime;
    }

    public StepEntity getLogEndTime() {
        return logEndTime;
    }

    public String getLogStatus() {
        return logStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLogInstance that = (StepLogInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(logDefinition, that.logDefinition) && Objects.equals(logEntries, that.logEntries) && logSize == that.logSize && Objects.equals(logStartTime, that.logStartTime) && Objects.equals(logEndTime, that.logEndTime) && Objects.equals(logStatus, that.logStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, logDefinition, logEntries, logSize, logStartTime, logEndTime, logStatus);
    }

    @Override
    public String toString() {
        return "StepLogInstance{" + "id=" + id + "name=" + name + "logDefinition=" + logDefinition + "logEntries=" + logEntries + "logSize=" + logSize + "logStartTime=" + logStartTime + "logEndTime=" + logEndTime + "logStatus=" + logStatus + "}";
    }
}