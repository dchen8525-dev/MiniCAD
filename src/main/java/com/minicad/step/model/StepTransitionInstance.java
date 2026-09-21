package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRANSITION_INSTANCE.
 * A transition instance entity.
 *
 * @param id STEP instance id
 * @param name transition instance name
 * @param transitionDefinition transition variance definition reference
 * @param transitionState transition variance state
 * @param transitionStartTime transition variance start time
 * @param transitionEndTime transition variance end time
 * @param transitionStatus transition variance status
 */
public final class StepTransitionInstance extends AbstractStepEntity {
    private final StepEntity transitionDefinition;
    private final String transitionState;
    private final StepEntity transitionStartTime;
    private final StepEntity transitionEndTime;
    private final String transitionStatus;

    public StepTransitionInstance(int id, String name, StepEntity transitionDefinition, String transitionState, StepEntity transitionStartTime, StepEntity transitionEndTime, String transitionStatus) {
        super(id, name);
        this.transitionDefinition = transitionDefinition;
        this.transitionState = transitionState;
        this.transitionStartTime = transitionStartTime;
        this.transitionEndTime = transitionEndTime;
        this.transitionStatus = transitionStatus;
    }

    public StepEntity getTransitionDefinition() {
        return transitionDefinition;
    }

    public String getTransitionState() {
        return transitionState;
    }

    public StepEntity getTransitionStartTime() {
        return transitionStartTime;
    }

    public StepEntity getTransitionEndTime() {
        return transitionEndTime;
    }

    public String getTransitionStatus() {
        return transitionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transitionDefinition", transitionDefinition);
        state.put("transitionState", transitionState);
        state.put("transitionStartTime", transitionStartTime);
        state.put("transitionEndTime", transitionEndTime);
        state.put("transitionStatus", transitionStatus);
        return state;
    }
}
