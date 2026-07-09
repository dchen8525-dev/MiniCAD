package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RECOVERY_INSTANCE.
 * A recovery instance entity.
 *
 * @param id STEP instance id
 * @param name recovery instance name
 * @param recoveryDefinition recovery variance definition reference
 * @param recoveryState recovery variance state
 * @param recoveryStartTime recovery variance start time
 * @param recoveryEndTime recovery variance end time
 * @param recoveryResult recovery variance result
 * @param recoveryStatus recovery variance status
 */
/**
 * Resolved RECOVERY_INSTANCE.
 * A recovery instance entity.
 *
 * @param id STEP instance id
 * @param name recovery instance name
 * @param recoveryDefinition recovery variance definition reference
 * @param recoveryState recovery variance state
 * @param recoveryStartTime recovery variance start time
 * @param recoveryEndTime recovery variance end time
 * @param recoveryResult recovery variance result
 * @param recoveryStatus recovery variance status
 */
public final class StepRecoveryInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity recoveryDefinition;
    private final String recoveryState;
    private final StepEntity recoveryStartTime;
    private final StepEntity recoveryEndTime;
    private final String recoveryResult;
    private final String recoveryStatus;

    public StepRecoveryInstance(int id, String name, StepEntity recoveryDefinition, String recoveryState, StepEntity recoveryStartTime, StepEntity recoveryEndTime, String recoveryResult, String recoveryStatus) {
        this.id = id;
        this.name = name;
        this.recoveryDefinition = recoveryDefinition;
        this.recoveryState = recoveryState;
        this.recoveryStartTime = recoveryStartTime;
        this.recoveryEndTime = recoveryEndTime;
        this.recoveryResult = recoveryResult;
        this.recoveryStatus = recoveryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRecoveryDefinition() {
        return recoveryDefinition;
    }

    public String getRecoveryState() {
        return recoveryState;
    }

    public StepEntity getRecoveryStartTime() {
        return recoveryStartTime;
    }

    public StepEntity getRecoveryEndTime() {
        return recoveryEndTime;
    }

    public String getRecoveryResult() {
        return recoveryResult;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRecoveryInstance that = (StepRecoveryInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(recoveryDefinition, that.recoveryDefinition) && Objects.equals(recoveryState, that.recoveryState) && Objects.equals(recoveryStartTime, that.recoveryStartTime) && Objects.equals(recoveryEndTime, that.recoveryEndTime) && Objects.equals(recoveryResult, that.recoveryResult) && Objects.equals(recoveryStatus, that.recoveryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recoveryDefinition, recoveryState, recoveryStartTime, recoveryEndTime, recoveryResult, recoveryStatus);
    }

    @Override
    public String toString() {
        return "StepRecoveryInstance{" + "id=" + id + "name=" + name + "recoveryDefinition=" + recoveryDefinition + "recoveryState=" + recoveryState + "recoveryStartTime=" + recoveryStartTime + "recoveryEndTime=" + recoveryEndTime + "recoveryResult=" + recoveryResult + "recoveryStatus=" + recoveryStatus + "}";
    }
}