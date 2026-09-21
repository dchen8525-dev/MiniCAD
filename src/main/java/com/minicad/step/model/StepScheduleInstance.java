package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepScheduleInstance extends AbstractStepEntity {
    private final StepEntity scheduleDefinition;
    private final double scheduleProgress;
    private final List<String> scheduleActuals;
    private final String scheduleStatus;

    public StepScheduleInstance(int id, String name, StepEntity scheduleDefinition, double scheduleProgress, List<String> scheduleActuals, String scheduleStatus) {
        super(id, name);
        this.scheduleDefinition = scheduleDefinition;
        this.scheduleProgress = scheduleProgress;
        this.scheduleActuals = scheduleActuals == null ? null : java.util.List.copyOf(scheduleActuals);
        this.scheduleStatus = scheduleStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("scheduleDefinition", scheduleDefinition);
        state.put("scheduleProgress", scheduleProgress);
        state.put("scheduleActuals", scheduleActuals);
        state.put("scheduleStatus", scheduleStatus);
        return state;
    }
}
