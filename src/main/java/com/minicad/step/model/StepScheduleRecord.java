package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepScheduleRecord extends AbstractStepEntity {
    private final String scheduleType;
    private final StepEntity scheduleTarget;
    private final StepEntity scheduleTime;
    private final StepEntity scheduleExecutionTime;
    private final String scheduleResult;
    private final String scheduleStatus;

    public StepScheduleRecord(int id, String name, String scheduleType, StepEntity scheduleTarget, StepEntity scheduleTime, StepEntity scheduleExecutionTime, String scheduleResult, String scheduleStatus) {
        super(id, name);
        this.scheduleType = scheduleType;
        this.scheduleTarget = scheduleTarget;
        this.scheduleTime = scheduleTime;
        this.scheduleExecutionTime = scheduleExecutionTime;
        this.scheduleResult = scheduleResult;
        this.scheduleStatus = scheduleStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "scheduleType", scheduleType,
                "scheduleTarget", scheduleTarget, "scheduleTime", scheduleTime,
                "scheduleExecutionTime", scheduleExecutionTime, "scheduleResult", scheduleResult,
                "scheduleStatus", scheduleStatus);
    }
}
