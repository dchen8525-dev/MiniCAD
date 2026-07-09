package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved EXECUTION_TRACE.
 * An execution trace entity.
 *
 * @param id STEP instance id
 * @param name trace name
 * @param traceType trace variance type
 * @param traceEntries trace variance trace entries
 * @param traceStartTime trace variance start time
 * @param traceEndTime trace variance end time
 * @param traceStatus trace variance status
 */
/**
 * Resolved EXECUTION_TRACE.
 * An execution trace entity.
 *
 * @param id STEP instance id
 * @param name trace name
 * @param traceType trace variance type
 * @param traceEntries trace variance trace entries
 * @param traceStartTime trace variance start time
 * @param traceEndTime trace variance end time
 * @param traceStatus trace variance status
 */
public final class StepExecutionTrace implements StepEntity {
    private final int id;
    private final String name;
    private final String traceType;
    private final List<String> traceEntries;
    private final StepEntity traceStartTime;
    private final StepEntity traceEndTime;
    private final String traceStatus;

    public StepExecutionTrace(int id, String name, String traceType, List<String> traceEntries, StepEntity traceStartTime, StepEntity traceEndTime, String traceStatus) {
        this.id = id;
        this.name = name;
        this.traceType = traceType;
        this.traceEntries = traceEntries == null ? null : java.util.List.copyOf(traceEntries);
        this.traceStartTime = traceStartTime;
        this.traceEndTime = traceEndTime;
        this.traceStatus = traceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTraceType() {
        return traceType;
    }

    public List<String> getTraceEntries() {
        return traceEntries;
    }

    public StepEntity getTraceStartTime() {
        return traceStartTime;
    }

    public StepEntity getTraceEndTime() {
        return traceEndTime;
    }

    public String getTraceStatus() {
        return traceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExecutionTrace that = (StepExecutionTrace) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(traceType, that.traceType) && Objects.equals(traceEntries, that.traceEntries) && Objects.equals(traceStartTime, that.traceStartTime) && Objects.equals(traceEndTime, that.traceEndTime) && Objects.equals(traceStatus, that.traceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, traceType, traceEntries, traceStartTime, traceEndTime, traceStatus);
    }

    @Override
    public String toString() {
        return "StepExecutionTrace{" + "id=" + id + "name=" + name + "traceType=" + traceType + "traceEntries=" + traceEntries + "traceStartTime=" + traceStartTime + "traceEndTime=" + traceEndTime + "traceStatus=" + traceStatus + "}";
    }
}