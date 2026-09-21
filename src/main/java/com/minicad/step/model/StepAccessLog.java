package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ACCESS_LOG.
 * An access log entity.
 *
 * @param id STEP instance id
 * @param name log name
 * @param logType log variance type
 * @param logEntries log variance entry list
 * @param logStartTime log variance start time
 * @param logEndTime log variance end time
 * @param logStatus log variance status
 */
public final class StepAccessLog extends AbstractStepEntity {
    private final String logType;
    private final List<StepEntity> logEntries;
    private final StepEntity logStartTime;
    private final StepEntity logEndTime;
    private final String logStatus;

    public StepAccessLog(int id, String name, String logType, List<StepEntity> logEntries, StepEntity logStartTime, StepEntity logEndTime, String logStatus) {
        super(id, name);
        this.logType = logType;
        this.logEntries = logEntries == null ? null : java.util.List.copyOf(logEntries);
        this.logStartTime = logStartTime;
        this.logEndTime = logEndTime;
        this.logStatus = logStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("logType", logType);
        state.put("logEntries", logEntries);
        state.put("logStartTime", logStartTime);
        state.put("logEndTime", logEndTime);
        state.put("logStatus", logStatus);
        return state;
    }
}
