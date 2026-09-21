package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepExecutionTrace extends AbstractStepEntity {
    private final String traceType;
    private final List<String> traceEntries;
    private final StepEntity traceStartTime;
    private final StepEntity traceEndTime;
    private final String traceStatus;

    public StepExecutionTrace(int id, String name, String traceType, List<String> traceEntries, StepEntity traceStartTime, StepEntity traceEndTime, String traceStatus) {
        super(id, name);
        this.traceType = traceType;
        this.traceEntries = traceEntries == null ? null : java.util.List.copyOf(traceEntries);
        this.traceStartTime = traceStartTime;
        this.traceEndTime = traceEndTime;
        this.traceStatus = traceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("traceType", traceType);
        state.put("traceEntries", traceEntries);
        state.put("traceStartTime", traceStartTime);
        state.put("traceEndTime", traceEndTime);
        state.put("traceStatus", traceStatus);
        return state;
    }
}
