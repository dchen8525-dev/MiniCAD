package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SCHEDULE_INSTANCE.
 * A schedule instance entity.
 *
 * @param id STEP instance id
 * @param name schedule instance name
 * @param scheduleDefinition schedule variance definition reference
 * @param scheduleProgress schedule variance progress
 * @param scheduleActuals schedule variance actual values
 * @param scheduleStatus schedule variance status
 */
/**
 * Resolved SCHEDULE_INSTANCE.
 * A schedule instance entity.
 *
 * @param id STEP instance id
 * @param name schedule instance name
 * @param scheduleDefinition schedule variance definition reference
 * @param scheduleProgress schedule variance progress
 * @param scheduleActuals schedule variance actual values
 * @param scheduleStatus schedule variance status
 */
public final class StepScheduleInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity scheduleDefinition;
    private final double scheduleProgress;
    private final List<String> scheduleActuals;
    private final String scheduleStatus;

    public StepScheduleInstance(int id, String name, StepEntity scheduleDefinition, double scheduleProgress, List<String> scheduleActuals, String scheduleStatus) {
        this.id = id;
        this.name = name;
        this.scheduleDefinition = scheduleDefinition;
        this.scheduleProgress = scheduleProgress;
        this.scheduleActuals = scheduleActuals == null ? null : java.util.List.copyOf(scheduleActuals);
        this.scheduleStatus = scheduleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getScheduleDefinition() {
        return scheduleDefinition;
    }

    public double getScheduleProgress() {
        return scheduleProgress;
    }

    public List<String> getScheduleActuals() {
        return scheduleActuals;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepScheduleInstance that = (StepScheduleInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(scheduleDefinition, that.scheduleDefinition) && scheduleProgress == that.scheduleProgress && Objects.equals(scheduleActuals, that.scheduleActuals) && Objects.equals(scheduleStatus, that.scheduleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, scheduleDefinition, scheduleProgress, scheduleActuals, scheduleStatus);
    }

    @Override
    public String toString() {
        return "StepScheduleInstance{" + "id=" + id + "name=" + name + "scheduleDefinition=" + scheduleDefinition + "scheduleProgress=" + scheduleProgress + "scheduleActuals=" + scheduleActuals + "scheduleStatus=" + scheduleStatus + "}";
    }
}