package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepLogInstance extends AbstractStepEntity {
    private final StepEntity logDefinition;
    private final List<String> logEntries;
    private final long logSize;
    private final StepEntity logStartTime;
    private final StepEntity logEndTime;
    private final String logStatus;

    public StepLogInstance(int id, String name, StepEntity logDefinition, List<String> logEntries, long logSize, StepEntity logStartTime, StepEntity logEndTime, String logStatus) {
        super(id, name);
        this.logDefinition = logDefinition;
        this.logEntries = logEntries == null ? null : java.util.List.copyOf(logEntries);
        this.logSize = logSize;
        this.logStartTime = logStartTime;
        this.logEndTime = logEndTime;
        this.logStatus = logStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "logDefinition", logDefinition, "logEntries", logEntries,
                "logSize", logSize, "logStartTime", logStartTime, "logEndTime", logEndTime,
                "logStatus", logStatus);
    }
}
