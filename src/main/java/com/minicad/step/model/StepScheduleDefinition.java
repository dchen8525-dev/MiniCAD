package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SCHEDULE_DEFINITION.
 * A schedule definition entity.
 *
 * @param id STEP instance id
 * @param name schedule name
 * @param scheduleType schedule variance type
 * @param scheduleMilestones schedule variance milestones
 * @param scheduleConstraints schedule variance constraints
 * @param scheduleResources schedule variance resources
 * @param scheduleStatus schedule variance status
 */
/**
 * Resolved SCHEDULE_DEFINITION.
 * A schedule definition entity.
 *
 * @param id STEP instance id
 * @param name schedule name
 * @param scheduleType schedule variance type
 * @param scheduleMilestones schedule variance milestones
 * @param scheduleConstraints schedule variance constraints
 * @param scheduleResources schedule variance resources
 * @param scheduleStatus schedule variance status
 */
public final class StepScheduleDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String scheduleType;
    private final List<StepEntity> scheduleMilestones;
    private final List<String> scheduleConstraints;
    private final List<StepEntity> scheduleResources;
    private final String scheduleStatus;

    public StepScheduleDefinition(int id, String name, String scheduleType, List<StepEntity> scheduleMilestones, List<String> scheduleConstraints, List<StepEntity> scheduleResources, String scheduleStatus) {
        this.id = id;
        this.name = name;
        this.scheduleType = scheduleType;
        this.scheduleMilestones = scheduleMilestones == null ? null : java.util.List.copyOf(scheduleMilestones);
        this.scheduleConstraints = scheduleConstraints == null ? null : java.util.List.copyOf(scheduleConstraints);
        this.scheduleResources = scheduleResources == null ? null : java.util.List.copyOf(scheduleResources);
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

    public List<StepEntity> getScheduleMilestones() {
        return scheduleMilestones;
    }

    public List<String> getScheduleConstraints() {
        return scheduleConstraints;
    }

    public List<StepEntity> getScheduleResources() {
        return scheduleResources;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepScheduleDefinition that = (StepScheduleDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(scheduleType, that.scheduleType) && Objects.equals(scheduleMilestones, that.scheduleMilestones) && Objects.equals(scheduleConstraints, that.scheduleConstraints) && Objects.equals(scheduleResources, that.scheduleResources) && Objects.equals(scheduleStatus, that.scheduleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, scheduleType, scheduleMilestones, scheduleConstraints, scheduleResources, scheduleStatus);
    }

    @Override
    public String toString() {
        return "StepScheduleDefinition{" + "id=" + id + "name=" + name + "scheduleType=" + scheduleType + "scheduleMilestones=" + scheduleMilestones + "scheduleConstraints=" + scheduleConstraints + "scheduleResources=" + scheduleResources + "scheduleStatus=" + scheduleStatus + "}";
    }
}