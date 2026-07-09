package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved WAIT_INSTANCE.
 * A wait instance entity.
 *
 * @param id STEP instance id
 * @param name wait instance name
 * @param waitDefinition wait variance definition reference
 * @param waitState wait variance state
 * @param waitStartTime wait variance start time
 * @param waitConditionMet wait variance condition met flag
 * @param waitStatus wait variance status
 */
/**
 * Resolved WAIT_INSTANCE.
 * A wait instance entity.
 *
 * @param id STEP instance id
 * @param name wait instance name
 * @param waitDefinition wait variance definition reference
 * @param waitState wait variance state
 * @param waitStartTime wait variance start time
 * @param waitConditionMet wait variance condition met flag
 * @param waitStatus wait variance status
 */
public final class StepWaitInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity waitDefinition;
    private final String waitState;
    private final StepEntity waitStartTime;
    private final boolean waitConditionMet;
    private final String waitStatus;

    public StepWaitInstance(int id, String name, StepEntity waitDefinition, String waitState, StepEntity waitStartTime, boolean waitConditionMet, String waitStatus) {
        this.id = id;
        this.name = name;
        this.waitDefinition = waitDefinition;
        this.waitState = waitState;
        this.waitStartTime = waitStartTime;
        this.waitConditionMet = waitConditionMet;
        this.waitStatus = waitStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getWaitDefinition() {
        return waitDefinition;
    }

    public String getWaitState() {
        return waitState;
    }

    public StepEntity getWaitStartTime() {
        return waitStartTime;
    }

    public boolean isWaitConditionMet() {
        return waitConditionMet;
    }

    public String getWaitStatus() {
        return waitStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWaitInstance that = (StepWaitInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(waitDefinition, that.waitDefinition) && Objects.equals(waitState, that.waitState) && Objects.equals(waitStartTime, that.waitStartTime) && waitConditionMet == that.waitConditionMet && Objects.equals(waitStatus, that.waitStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, waitDefinition, waitState, waitStartTime, waitConditionMet, waitStatus);
    }

    @Override
    public String toString() {
        return "StepWaitInstance{" + "id=" + id + "name=" + name + "waitDefinition=" + waitDefinition + "waitState=" + waitState + "waitStartTime=" + waitStartTime + "waitConditionMet=" + waitConditionMet + "waitStatus=" + waitStatus + "}";
    }
}