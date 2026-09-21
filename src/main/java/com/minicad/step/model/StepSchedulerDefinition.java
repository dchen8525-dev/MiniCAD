package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SCHEDULER_DEFINITION.
 * A scheduler definition entity.
 *
 * @param id STEP instance id
 * @param name scheduler name
 * @param schedulerType scheduler variance type
 * @param schedulerPolicy scheduler variance scheduling policy
 * @param schedulerInterval scheduler variance interval
 * @param schedulerJobs scheduler variance job definitions
 * @param schedulerStatus scheduler variance status
 */
public final class StepSchedulerDefinition extends AbstractStepEntity {
    private final String schedulerType;
    private final String schedulerPolicy;
    private final int schedulerInterval;
    private final List<StepEntity> schedulerJobs;
    private final String schedulerStatus;

    public StepSchedulerDefinition(int id, String name, String schedulerType, String schedulerPolicy, int schedulerInterval, List<StepEntity> schedulerJobs, String schedulerStatus) {
        super(id, name);
        this.schedulerType = schedulerType;
        this.schedulerPolicy = schedulerPolicy;
        this.schedulerInterval = schedulerInterval;
        this.schedulerJobs = schedulerJobs == null ? null : java.util.List.copyOf(schedulerJobs);
        this.schedulerStatus = schedulerStatus;
    }

    public String getSchedulerType() {
        return schedulerType;
    }

    public String getSchedulerPolicy() {
        return schedulerPolicy;
    }

    public int getSchedulerInterval() {
        return schedulerInterval;
    }

    public List<StepEntity> getSchedulerJobs() {
        return schedulerJobs;
    }

    public String getSchedulerStatus() {
        return schedulerStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("schedulerType", schedulerType);
        state.put("schedulerPolicy", schedulerPolicy);
        state.put("schedulerInterval", schedulerInterval);
        state.put("schedulerJobs", schedulerJobs);
        state.put("schedulerStatus", schedulerStatus);
        return state;
    }
}
