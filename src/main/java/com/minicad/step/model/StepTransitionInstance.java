package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTransitionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity transitionDefinition;
    private final String transitionState;
    private final StepEntity transitionStartTime;
    private final StepEntity transitionEndTime;
    private final String transitionStatus;

    public StepTransitionInstance(int id, String name, StepEntity transitionDefinition, String transitionState, StepEntity transitionStartTime, StepEntity transitionEndTime, String transitionStatus) {
        this.id = id;
        this.name = name;
        this.transitionDefinition = transitionDefinition;
        this.transitionState = transitionState;
        this.transitionStartTime = transitionStartTime;
        this.transitionEndTime = transitionEndTime;
        this.transitionStatus = transitionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransitionInstance that = (StepTransitionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transitionDefinition, that.transitionDefinition) && Objects.equals(transitionState, that.transitionState) && Objects.equals(transitionStartTime, that.transitionStartTime) && Objects.equals(transitionEndTime, that.transitionEndTime) && Objects.equals(transitionStatus, that.transitionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transitionDefinition, transitionState, transitionStartTime, transitionEndTime, transitionStatus);
    }

    @Override
    public String toString() {
        return "StepTransitionInstance{" + "id=" + id + "name=" + name + "transitionDefinition=" + transitionDefinition + "transitionState=" + transitionState + "transitionStartTime=" + transitionStartTime + "transitionEndTime=" + transitionEndTime + "transitionStatus=" + transitionStatus + "}";
    }
}