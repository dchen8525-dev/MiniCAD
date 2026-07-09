package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSchedulerInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity schedulerDefinition;
    private final String schedulerState;
    private final int schedulerActiveJobs;
    private final int schedulerPendingJobs;
    private final int schedulerCompletedJobs;
    private final String schedulerStatus;

    public StepSchedulerInstance(int id, String name, StepEntity schedulerDefinition, String schedulerState, int schedulerActiveJobs, int schedulerPendingJobs, int schedulerCompletedJobs, String schedulerStatus) {
        this.id = id;
        this.name = name;
        this.schedulerDefinition = schedulerDefinition;
        this.schedulerState = schedulerState;
        this.schedulerActiveJobs = schedulerActiveJobs;
        this.schedulerPendingJobs = schedulerPendingJobs;
        this.schedulerCompletedJobs = schedulerCompletedJobs;
        this.schedulerStatus = schedulerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSchedulerInstance that = (StepSchedulerInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(schedulerDefinition, that.schedulerDefinition) && Objects.equals(schedulerState, that.schedulerState) && schedulerActiveJobs == that.schedulerActiveJobs && schedulerPendingJobs == that.schedulerPendingJobs && schedulerCompletedJobs == that.schedulerCompletedJobs && Objects.equals(schedulerStatus, that.schedulerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, schedulerDefinition, schedulerState, schedulerActiveJobs, schedulerPendingJobs, schedulerCompletedJobs, schedulerStatus);
    }

    @Override
    public String toString() {
        return "StepSchedulerInstance{" + "id=" + id + "name=" + name + "schedulerDefinition=" + schedulerDefinition + "schedulerState=" + schedulerState + "schedulerActiveJobs=" + schedulerActiveJobs + "schedulerPendingJobs=" + schedulerPendingJobs + "schedulerCompletedJobs=" + schedulerCompletedJobs + "schedulerStatus=" + schedulerStatus + "}";
    }
}