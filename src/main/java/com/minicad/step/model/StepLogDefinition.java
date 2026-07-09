package com.minicad.step.model.management.log;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepLogDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String logType;
    private final String logLevel;
    private final String logFormat;
    private final int logRetention;
    private final String logStatus;

    public StepLogDefinition(int id, String name, String logType, String logLevel, String logFormat, int logRetention, String logStatus) {
        this.id = id;
        this.name = name;
        this.logType = logType;
        this.logLevel = logLevel;
        this.logFormat = logFormat;
        this.logRetention = logRetention;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLogDefinition that = (StepLogDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(logType, that.logType) && Objects.equals(logLevel, that.logLevel) && Objects.equals(logFormat, that.logFormat) && logRetention == that.logRetention && Objects.equals(logStatus, that.logStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, logType, logLevel, logFormat, logRetention, logStatus);
    }

    @Override
    public String toString() {
        return "StepLogDefinition{" + "id=" + id + "name=" + name + "logType=" + logType + "logLevel=" + logLevel + "logFormat=" + logFormat + "logRetention=" + logRetention + "logStatus=" + logStatus + "}";
    }
}