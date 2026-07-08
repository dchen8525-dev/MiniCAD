package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SCHEDULE_RECORD.
 * A schedule record entity.
 *
 * @param id STEP instance id
 * @param name schedule name
 * @param scheduleType schedule variance type
 * @param scheduleTarget schedule variance target reference
 * @param scheduleTime schedule variance scheduled time
 * @param scheduleExecutionTime schedule variance execution time
 * @param scheduleResult schedule variance result
 * @param scheduleStatus schedule variance status
 */
/**
 * Resolved SCHEDULE_RECORD.
 * A schedule record entity.
 *
 * @param id STEP instance id
 * @param name schedule name
 * @param scheduleType schedule variance type
 * @param scheduleTarget schedule variance target reference
 * @param scheduleTime schedule variance scheduled time
 * @param scheduleExecutionTime schedule variance execution time
 * @param scheduleResult schedule variance result
 * @param scheduleStatus schedule variance status
 */
public final class StepScheduleRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String scheduleType;
    private final StepEntity scheduleTarget;
    private final StepEntity scheduleTime;
    private final StepEntity scheduleExecutionTime;
    private final String scheduleResult;
    private final String scheduleStatus;

    public StepScheduleRecord(int id, String name, String scheduleType, StepEntity scheduleTarget, StepEntity scheduleTime, StepEntity scheduleExecutionTime, String scheduleResult, String scheduleStatus) {
        this.id = id;
        this.name = name;
        this.scheduleType = scheduleType;
        this.scheduleTarget = scheduleTarget;
        this.scheduleTime = scheduleTime;
        this.scheduleExecutionTime = scheduleExecutionTime;
        this.scheduleResult = scheduleResult;
        this.scheduleStatus = scheduleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public StepEntity getScheduleTarget() {
        return scheduleTarget;
    }

    public StepEntity getScheduleTime() {
        return scheduleTime;
    }

    public StepEntity getScheduleExecutionTime() {
        return scheduleExecutionTime;
    }

    public String getScheduleResult() {
        return scheduleResult;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepScheduleRecord that = (StepScheduleRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(scheduleType, that.scheduleType) && Objects.equals(scheduleTarget, that.scheduleTarget) && Objects.equals(scheduleTime, that.scheduleTime) && Objects.equals(scheduleExecutionTime, that.scheduleExecutionTime) && Objects.equals(scheduleResult, that.scheduleResult) && Objects.equals(scheduleStatus, that.scheduleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, scheduleType, scheduleTarget, scheduleTime, scheduleExecutionTime, scheduleResult, scheduleStatus);
    }

    @Override
    public String toString() {
        return "StepScheduleRecord{" + "id=" + id + "name=" + name + "scheduleType=" + scheduleType + "scheduleTarget=" + scheduleTarget + "scheduleTime=" + scheduleTime + "scheduleExecutionTime=" + scheduleExecutionTime + "scheduleResult=" + scheduleResult + "scheduleStatus=" + scheduleStatus + "}";
    }
}