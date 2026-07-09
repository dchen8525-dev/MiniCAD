package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TIMER_INSTANCE.
 * A timer instance entity.
 *
 * @param id STEP instance id
 * @param name timer instance name
 * @param timerDefinition timer variance definition reference
 * @param timerState timer variance state
 * @param timerStartTime timer variance start time
 * @param timerRemaining timer variance remaining time
 * @param timerStatus timer variance status
 */
/**
 * Resolved TIMER_INSTANCE.
 * A timer instance entity.
 *
 * @param id STEP instance id
 * @param name timer instance name
 * @param timerDefinition timer variance definition reference
 * @param timerState timer variance state
 * @param timerStartTime timer variance start time
 * @param timerRemaining timer variance remaining time
 * @param timerStatus timer variance status
 */
public final class StepTimerInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity timerDefinition;
    private final String timerState;
    private final StepEntity timerStartTime;
    private final int timerRemaining;
    private final String timerStatus;

    public StepTimerInstance(int id, String name, StepEntity timerDefinition, String timerState, StepEntity timerStartTime, int timerRemaining, String timerStatus) {
        this.id = id;
        this.name = name;
        this.timerDefinition = timerDefinition;
        this.timerState = timerState;
        this.timerStartTime = timerStartTime;
        this.timerRemaining = timerRemaining;
        this.timerStatus = timerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTimerDefinition() {
        return timerDefinition;
    }

    public String getTimerState() {
        return timerState;
    }

    public StepEntity getTimerStartTime() {
        return timerStartTime;
    }

    public int getTimerRemaining() {
        return timerRemaining;
    }

    public String getTimerStatus() {
        return timerStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTimerInstance that = (StepTimerInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(timerDefinition, that.timerDefinition) && Objects.equals(timerState, that.timerState) && Objects.equals(timerStartTime, that.timerStartTime) && timerRemaining == that.timerRemaining && Objects.equals(timerStatus, that.timerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, timerDefinition, timerState, timerStartTime, timerRemaining, timerStatus);
    }

    @Override
    public String toString() {
        return "StepTimerInstance{" + "id=" + id + "name=" + name + "timerDefinition=" + timerDefinition + "timerState=" + timerState + "timerStartTime=" + timerStartTime + "timerRemaining=" + timerRemaining + "timerStatus=" + timerStatus + "}";
    }
}