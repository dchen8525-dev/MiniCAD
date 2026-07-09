package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PAUSE_INSTANCE.
 * A pause instance entity.
 *
 * @param id STEP instance id
 * @param name pause instance name
 * @param pauseDefinition pause variance definition reference
 * @param pauseState pause variance state
 * @param pauseStartTime pause variance start time
 * @param pauseDuration pause variance current duration
 * @param pauseStatus pause variance status
 */
/**
 * Resolved PAUSE_INSTANCE.
 * A pause instance entity.
 *
 * @param id STEP instance id
 * @param name pause instance name
 * @param pauseDefinition pause variance definition reference
 * @param pauseState pause variance state
 * @param pauseStartTime pause variance start time
 * @param pauseDuration pause variance current duration
 * @param pauseStatus pause variance status
 */
public final class StepPauseInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity pauseDefinition;
    private final String pauseState;
    private final StepEntity pauseStartTime;
    private final int pauseDuration;
    private final String pauseStatus;

    public StepPauseInstance(int id, String name, StepEntity pauseDefinition, String pauseState, StepEntity pauseStartTime, int pauseDuration, String pauseStatus) {
        this.id = id;
        this.name = name;
        this.pauseDefinition = pauseDefinition;
        this.pauseState = pauseState;
        this.pauseStartTime = pauseStartTime;
        this.pauseDuration = pauseDuration;
        this.pauseStatus = pauseStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPauseDefinition() {
        return pauseDefinition;
    }

    public String getPauseState() {
        return pauseState;
    }

    public StepEntity getPauseStartTime() {
        return pauseStartTime;
    }

    public int getPauseDuration() {
        return pauseDuration;
    }

    public String getPauseStatus() {
        return pauseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPauseInstance that = (StepPauseInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pauseDefinition, that.pauseDefinition) && Objects.equals(pauseState, that.pauseState) && Objects.equals(pauseStartTime, that.pauseStartTime) && pauseDuration == that.pauseDuration && Objects.equals(pauseStatus, that.pauseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pauseDefinition, pauseState, pauseStartTime, pauseDuration, pauseStatus);
    }

    @Override
    public String toString() {
        return "StepPauseInstance{" + "id=" + id + "name=" + name + "pauseDefinition=" + pauseDefinition + "pauseState=" + pauseState + "pauseStartTime=" + pauseStartTime + "pauseDuration=" + pauseDuration + "pauseStatus=" + pauseStatus + "}";
    }
}