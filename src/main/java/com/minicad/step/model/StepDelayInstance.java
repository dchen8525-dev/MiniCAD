package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DELAY_INSTANCE.
 * A delay instance entity.
 *
 * @param id STEP instance id
 * @param name delay instance name
 * @param delayDefinition delay variance definition reference
 * @param delayState delay variance state
 * @param delayStartTime delay variance start time
 * @param delayEndTime delay variance expected end time
 * @param delayRemaining delay variance remaining time
 * @param delayStatus delay variance status
 */
/**
 * Resolved DELAY_INSTANCE.
 * A delay instance entity.
 *
 * @param id STEP instance id
 * @param name delay instance name
 * @param delayDefinition delay variance definition reference
 * @param delayState delay variance state
 * @param delayStartTime delay variance start time
 * @param delayEndTime delay variance expected end time
 * @param delayRemaining delay variance remaining time
 * @param delayStatus delay variance status
 */
public final class StepDelayInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity delayDefinition;
    private final String delayState;
    private final StepEntity delayStartTime;
    private final StepEntity delayEndTime;
    private final int delayRemaining;
    private final String delayStatus;

    public StepDelayInstance(int id, String name, StepEntity delayDefinition, String delayState, StepEntity delayStartTime, StepEntity delayEndTime, int delayRemaining, String delayStatus) {
        this.id = id;
        this.name = name;
        this.delayDefinition = delayDefinition;
        this.delayState = delayState;
        this.delayStartTime = delayStartTime;
        this.delayEndTime = delayEndTime;
        this.delayRemaining = delayRemaining;
        this.delayStatus = delayStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getDelayDefinition() {
        return delayDefinition;
    }

    public String getDelayState() {
        return delayState;
    }

    public StepEntity getDelayStartTime() {
        return delayStartTime;
    }

    public StepEntity getDelayEndTime() {
        return delayEndTime;
    }

    public int getDelayRemaining() {
        return delayRemaining;
    }

    public String getDelayStatus() {
        return delayStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDelayInstance that = (StepDelayInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(delayDefinition, that.delayDefinition) && Objects.equals(delayState, that.delayState) && Objects.equals(delayStartTime, that.delayStartTime) && Objects.equals(delayEndTime, that.delayEndTime) && delayRemaining == that.delayRemaining && Objects.equals(delayStatus, that.delayStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, delayDefinition, delayState, delayStartTime, delayEndTime, delayRemaining, delayStatus);
    }

    @Override
    public String toString() {
        return "StepDelayInstance{" + "id=" + id + "name=" + name + "delayDefinition=" + delayDefinition + "delayState=" + delayState + "delayStartTime=" + delayStartTime + "delayEndTime=" + delayEndTime + "delayRemaining=" + delayRemaining + "delayStatus=" + delayStatus + "}";
    }
}