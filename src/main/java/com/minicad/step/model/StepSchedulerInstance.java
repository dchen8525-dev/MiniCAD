package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SCHEDULER_INSTANCE.
 * A scheduler instance entity.
 *
 * @param id STEP instance id
 * @param name scheduler instance name
 * @param schedulerDefinition scheduler variance definition reference
 * @param schedulerState scheduler variance state
 * @param schedulerActiveJobs scheduler variance active job count
 * @param schedulerPendingJobs scheduler variance pending job count
 * @param schedulerCompletedJobs scheduler variance completed job count
 * @param schedulerStatus scheduler variance status
 */
public final class StepSchedulerInstance extends AbstractStepEntity {
    private final StepEntity schedulerDefinition;
    private final String schedulerState;
    private final int schedulerActiveJobs;
    private final int schedulerPendingJobs;
    private final int schedulerCompletedJobs;
    private final String schedulerStatus;

    public StepSchedulerInstance(int id, String name, StepEntity schedulerDefinition, String schedulerState, int schedulerActiveJobs, int schedulerPendingJobs, int schedulerCompletedJobs, String schedulerStatus) {
        super(id, name);
        this.schedulerDefinition = schedulerDefinition;
        this.schedulerState = schedulerState;
        this.schedulerActiveJobs = schedulerActiveJobs;
        this.schedulerPendingJobs = schedulerPendingJobs;
        this.schedulerCompletedJobs = schedulerCompletedJobs;
        this.schedulerStatus = schedulerStatus;
    }

    public StepEntity getSchedulerDefinition() {
        return schedulerDefinition;
    }

    public String getSchedulerState() {
        return schedulerState;
    }

    public int getSchedulerActiveJobs() {
        return schedulerActiveJobs;
    }

    public int getSchedulerPendingJobs() {
        return schedulerPendingJobs;
    }

    public int getSchedulerCompletedJobs() {
        return schedulerCompletedJobs;
    }

    public String getSchedulerStatus() {
        return schedulerStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("schedulerDefinition", schedulerDefinition);
        state.put("schedulerState", schedulerState);
        state.put("schedulerActiveJobs", schedulerActiveJobs);
        state.put("schedulerPendingJobs", schedulerPendingJobs);
        state.put("schedulerCompletedJobs", schedulerCompletedJobs);
        state.put("schedulerStatus", schedulerStatus);
        return state;
    }
}
