package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LOG_DEFINITION.
 * A log definition entity.
 *
 * @param id STEP instance id
 * @param name log name
 * @param logType log variance type
 * @param logLevel log variance level
 * @param logFormat log variance format
 * @param logRetention log variance retention period
 * @param logStatus log variance status
 */
public final class StepLogDefinition extends AbstractStepEntity {
    private final String logType;
    private final String logLevel;
    private final String logFormat;
    private final int logRetention;
    private final String logStatus;

    public StepLogDefinition(int id, String name, String logType, String logLevel, String logFormat, int logRetention, String logStatus) {
        super(id, name);
        this.logType = logType;
        this.logLevel = logLevel;
        this.logFormat = logFormat;
        this.logRetention = logRetention;
        this.logStatus = logStatus;
    }

    public String getLogType() {
        return logType;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getLogFormat() {
        return logFormat;
    }

    public int getLogRetention() {
        return logRetention;
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
        state.put("logLevel", logLevel);
        state.put("logFormat", logFormat);
        state.put("logRetention", logRetention);
        state.put("logStatus", logStatus);
        return state;
    }
}
