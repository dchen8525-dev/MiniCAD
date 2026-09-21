package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepScheduleDefinition extends AbstractStepEntity {
    private final String scheduleType;
    private final List<StepEntity> scheduleMilestones;
    private final List<String> scheduleConstraints;
    private final List<StepEntity> scheduleResources;
    private final String scheduleStatus;

    public StepScheduleDefinition(int id, String name, String scheduleType, List<StepEntity> scheduleMilestones, List<String> scheduleConstraints, List<StepEntity> scheduleResources, String scheduleStatus) {
        super(id, name);
        this.scheduleType = scheduleType;
        this.scheduleMilestones = scheduleMilestones == null ? null : java.util.List.copyOf(scheduleMilestones);
        this.scheduleConstraints = scheduleConstraints == null ? null : java.util.List.copyOf(scheduleConstraints);
        this.scheduleResources = scheduleResources == null ? null : java.util.List.copyOf(scheduleResources);
        this.scheduleStatus = scheduleStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("scheduleType", scheduleType);
        state.put("scheduleMilestones", scheduleMilestones);
        state.put("scheduleConstraints", scheduleConstraints);
        state.put("scheduleResources", scheduleResources);
        state.put("scheduleStatus", scheduleStatus);
        return state;
    }
}
