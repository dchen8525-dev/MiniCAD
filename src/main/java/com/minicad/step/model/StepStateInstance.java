package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STATE_INSTANCE.
 * A state instance entity.
 *
 * @param id STEP instance id
 * @param name state instance name
 * @param stateDefinition state variance definition reference
 * @param stateActive state variance active flag
 * @param stateEntryTime state variance entry time
 * @param stateDuration state variance duration
 * @param stateStatus state variance status
 */
/**
 * Resolved STATE_INSTANCE.
 * A state instance entity.
 *
 * @param id STEP instance id
 * @param name state instance name
 * @param stateDefinition state variance definition reference
 * @param stateActive state variance active flag
 * @param stateEntryTime state variance entry time
 * @param stateDuration state variance duration
 * @param stateStatus state variance status
 */
public final class StepStateInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity stateDefinition;
    private final boolean stateActive;
    private final StepEntity stateEntryTime;
    private final int stateDuration;
    private final String stateStatus;

    public StepStateInstance(int id, String name, StepEntity stateDefinition, boolean stateActive, StepEntity stateEntryTime, int stateDuration, String stateStatus) {
        this.id = id;
        this.name = name;
        this.stateDefinition = stateDefinition;
        this.stateActive = stateActive;
        this.stateEntryTime = stateEntryTime;
        this.stateDuration = stateDuration;
        this.stateStatus = stateStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStateDefinition() {
        return stateDefinition;
    }

    public boolean isStateActive() {
        return stateActive;
    }

    public StepEntity getStateEntryTime() {
        return stateEntryTime;
    }

    public int getStateDuration() {
        return stateDuration;
    }

    public String getStateStatus() {
        return stateStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStateInstance that = (StepStateInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(stateDefinition, that.stateDefinition) && stateActive == that.stateActive && Objects.equals(stateEntryTime, that.stateEntryTime) && stateDuration == that.stateDuration && Objects.equals(stateStatus, that.stateStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stateDefinition, stateActive, stateEntryTime, stateDuration, stateStatus);
    }

    @Override
    public String toString() {
        return "StepStateInstance{" + "id=" + id + "name=" + name + "stateDefinition=" + stateDefinition + "stateActive=" + stateActive + "stateEntryTime=" + stateEntryTime + "stateDuration=" + stateDuration + "stateStatus=" + stateStatus + "}";
    }
}