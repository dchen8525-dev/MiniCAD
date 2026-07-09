package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TIMEOUT_INSTANCE.
 * A timeout instance entity.
 *
 * @param id STEP instance id
 * @param name timeout instance name
 * @param timeoutDefinition timeout variance definition reference
 * @param timeoutState timeout variance state
 * @param timeoutStartTime timeout variance start time
 * @param timeoutTriggered timeout variance triggered flag
 * @param timeoutStatus timeout variance status
 */
/**
 * Resolved TIMEOUT_INSTANCE.
 * A timeout instance entity.
 *
 * @param id STEP instance id
 * @param name timeout instance name
 * @param timeoutDefinition timeout variance definition reference
 * @param timeoutState timeout variance state
 * @param timeoutStartTime timeout variance start time
 * @param timeoutTriggered timeout variance triggered flag
 * @param timeoutStatus timeout variance status
 */
public final class StepTimeoutInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity timeoutDefinition;
    private final String timeoutState;
    private final StepEntity timeoutStartTime;
    private final boolean timeoutTriggered;
    private final String timeoutStatus;

    public StepTimeoutInstance(int id, String name, StepEntity timeoutDefinition, String timeoutState, StepEntity timeoutStartTime, boolean timeoutTriggered, String timeoutStatus) {
        this.id = id;
        this.name = name;
        this.timeoutDefinition = timeoutDefinition;
        this.timeoutState = timeoutState;
        this.timeoutStartTime = timeoutStartTime;
        this.timeoutTriggered = timeoutTriggered;
        this.timeoutStatus = timeoutStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTimeoutDefinition() {
        return timeoutDefinition;
    }

    public String getTimeoutState() {
        return timeoutState;
    }

    public StepEntity getTimeoutStartTime() {
        return timeoutStartTime;
    }

    public boolean isTimeoutTriggered() {
        return timeoutTriggered;
    }

    public String getTimeoutStatus() {
        return timeoutStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTimeoutInstance that = (StepTimeoutInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(timeoutDefinition, that.timeoutDefinition) && Objects.equals(timeoutState, that.timeoutState) && Objects.equals(timeoutStartTime, that.timeoutStartTime) && timeoutTriggered == that.timeoutTriggered && Objects.equals(timeoutStatus, that.timeoutStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, timeoutDefinition, timeoutState, timeoutStartTime, timeoutTriggered, timeoutStatus);
    }

    @Override
    public String toString() {
        return "StepTimeoutInstance{" + "id=" + id + "name=" + name + "timeoutDefinition=" + timeoutDefinition + "timeoutState=" + timeoutState + "timeoutStartTime=" + timeoutStartTime + "timeoutTriggered=" + timeoutTriggered + "timeoutStatus=" + timeoutStatus + "}";
    }
}