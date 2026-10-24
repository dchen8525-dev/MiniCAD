package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepStateInstance extends AbstractStepEntity {
    private final StepEntity stateDefinition;
    private final boolean stateActive;
    private final StepEntity stateEntryTime;
    private final int stateDuration;
    private final String stateStatus;

    public StepStateInstance(int id, String name, StepEntity stateDefinition, boolean stateActive, StepEntity stateEntryTime, int stateDuration, String stateStatus) {
        super(id, name);
        this.stateDefinition = stateDefinition;
        this.stateActive = stateActive;
        this.stateEntryTime = stateEntryTime;
        this.stateDuration = stateDuration;
        this.stateStatus = stateStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "stateDefinition", stateDefinition,
                "stateActive", stateActive, "stateEntryTime", stateEntryTime, "stateDuration", stateDuration,
                "stateStatus", stateStatus);
    }
}
