package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ERROR_LOG.
 * An error log entity.
 *
 * @param id STEP instance id
 * @param name log name
 * @param logType log variance type
 * @param logEntries log variance entry list
 * @param logStartTime log variance start time
 * @param logEndTime log variance end time
 * @param logStatus log variance status
 */
/**
 * Resolved ERROR_LOG.
 * An error log entity.
 *
 * @param id STEP instance id
 * @param name log name
 * @param logType log variance type
 * @param logEntries log variance entry list
 * @param logStartTime log variance start time
 * @param logEndTime log variance end time
 * @param logStatus log variance status
 */
public final class StepErrorLog implements StepEntity {
    private final int id;
    private final String name;
    private final String logType;
    private final List<StepEntity> logEntries;
    private final StepEntity logStartTime;
    private final StepEntity logEndTime;
    private final String logStatus;

    public StepErrorLog(int id, String name, String logType, List<StepEntity> logEntries, StepEntity logStartTime, StepEntity logEndTime, String logStatus) {
        this.id = id;
        this.name = name;
        this.logType = logType;
        this.logEntries = logEntries == null ? null : java.util.List.copyOf(logEntries);
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

    public String getLogType() {
        return logType;
    }

    public List<StepEntity> getLogEntries() {
        return logEntries;
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
        StepErrorLog that = (StepErrorLog) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(logType, that.logType) && Objects.equals(logEntries, that.logEntries) && Objects.equals(logStartTime, that.logStartTime) && Objects.equals(logEndTime, that.logEndTime) && Objects.equals(logStatus, that.logStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, logType, logEntries, logStartTime, logEndTime, logStatus);
    }

    @Override
    public String toString() {
        return "StepErrorLog{" + "id=" + id + "name=" + name + "logType=" + logType + "logEntries=" + logEntries + "logStartTime=" + logStartTime + "logEndTime=" + logEndTime + "logStatus=" + logStatus + "}";
    }
}