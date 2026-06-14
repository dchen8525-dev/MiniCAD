package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STAGE_INSTANCE.
 * A stage instance entity.
 *
 * @param id STEP instance id
 * @param name stage instance name
 * @param stageDefinition stage variance definition reference
 * @param stageState stage variance state
 * @param stageStartTime stage variance start time
 * @param stageEndTime stage variance end time
 * @param stageProgress stage variance progress percentage
 * @param stageStatus stage variance status
 */
/**
 * Resolved STAGE_INSTANCE.
 * A stage instance entity.
 *
 * @param id STEP instance id
 * @param name stage instance name
 * @param stageDefinition stage variance definition reference
 * @param stageState stage variance state
 * @param stageStartTime stage variance start time
 * @param stageEndTime stage variance end time
 * @param stageProgress stage variance progress percentage
 * @param stageStatus stage variance status
 */
public final class StepStageInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity stageDefinition;
    private final String stageState;
    private final StepEntity stageStartTime;
    private final StepEntity stageEndTime;
    private final double stageProgress;
    private final String stageStatus;

    public StepStageInstance(int id, String name, StepEntity stageDefinition, String stageState, StepEntity stageStartTime, StepEntity stageEndTime, double stageProgress, String stageStatus) {
        this.id = id;
        this.name = name;
        this.stageDefinition = stageDefinition;
        this.stageState = stageState;
        this.stageStartTime = stageStartTime;
        this.stageEndTime = stageEndTime;
        this.stageProgress = stageProgress;
        this.stageStatus = stageStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStageDefinition() {
        return stageDefinition;
    }

    public String getStageState() {
        return stageState;
    }

    public StepEntity getStageStartTime() {
        return stageStartTime;
    }

    public StepEntity getStageEndTime() {
        return stageEndTime;
    }

    public double getStageProgress() {
        return stageProgress;
    }

    public String getStageStatus() {
        return stageStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStageInstance that = (StepStageInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(stageDefinition, that.stageDefinition) && Objects.equals(stageState, that.stageState) && Objects.equals(stageStartTime, that.stageStartTime) && Objects.equals(stageEndTime, that.stageEndTime) && stageProgress == that.stageProgress && Objects.equals(stageStatus, that.stageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stageDefinition, stageState, stageStartTime, stageEndTime, stageProgress, stageStatus);
    }

    @Override
    public String toString() {
        return "StepStageInstance{" + "id=" + id + "name=" + name + "stageDefinition=" + stageDefinition + "stageState=" + stageState + "stageStartTime=" + stageStartTime + "stageEndTime=" + stageEndTime + "stageProgress=" + stageProgress + "stageStatus=" + stageStatus + "}";
    }
}