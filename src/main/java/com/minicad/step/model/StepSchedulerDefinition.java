package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSchedulerDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String schedulerType;
    private final String schedulerPolicy;
    private final int schedulerInterval;
    private final List<StepEntity> schedulerJobs;
    private final String schedulerStatus;

    public StepSchedulerDefinition(int id, String name, String schedulerType, String schedulerPolicy, int schedulerInterval, List<StepEntity> schedulerJobs, String schedulerStatus) {
        this.id = id;
        this.name = name;
        this.schedulerType = schedulerType;
        this.schedulerPolicy = schedulerPolicy;
        this.schedulerInterval = schedulerInterval;
        this.schedulerJobs = schedulerJobs == null ? null : java.util.List.copyOf(schedulerJobs);
        this.schedulerStatus = schedulerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSchedulerDefinition that = (StepSchedulerDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(schedulerType, that.schedulerType) && Objects.equals(schedulerPolicy, that.schedulerPolicy) && schedulerInterval == that.schedulerInterval && Objects.equals(schedulerJobs, that.schedulerJobs) && Objects.equals(schedulerStatus, that.schedulerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, schedulerType, schedulerPolicy, schedulerInterval, schedulerJobs, schedulerStatus);
    }

    @Override
    public String toString() {
        return "StepSchedulerDefinition{" + "id=" + id + "name=" + name + "schedulerType=" + schedulerType + "schedulerPolicy=" + schedulerPolicy + "schedulerInterval=" + schedulerInterval + "schedulerJobs=" + schedulerJobs + "schedulerStatus=" + schedulerStatus + "}";
    }
}