package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PIPELINE_INSTANCE.
 * A pipeline instance entity.
 *
 * @param id STEP instance id
 * @param name pipeline instance name
 * @param pipelineDefinition pipeline variance definition reference
 * @param pipelineState pipeline variance state
 * @param pipelineCurrentStage pipeline variance current stage
 * @param pipelineStartTime pipeline variance start time
 * @param pipelineEndTime pipeline variance end time
 * @param pipelineStatus pipeline variance status
 */
/**
 * Resolved PIPELINE_INSTANCE.
 * A pipeline instance entity.
 *
 * @param id STEP instance id
 * @param name pipeline instance name
 * @param pipelineDefinition pipeline variance definition reference
 * @param pipelineState pipeline variance state
 * @param pipelineCurrentStage pipeline variance current stage
 * @param pipelineStartTime pipeline variance start time
 * @param pipelineEndTime pipeline variance end time
 * @param pipelineStatus pipeline variance status
 */
public final class StepPipelineInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity pipelineDefinition;
    private final String pipelineState;
    private final int pipelineCurrentStage;
    private final StepEntity pipelineStartTime;
    private final StepEntity pipelineEndTime;
    private final String pipelineStatus;

    public StepPipelineInstance(int id, String name, StepEntity pipelineDefinition, String pipelineState, int pipelineCurrentStage, StepEntity pipelineStartTime, StepEntity pipelineEndTime, String pipelineStatus) {
        this.id = id;
        this.name = name;
        this.pipelineDefinition = pipelineDefinition;
        this.pipelineState = pipelineState;
        this.pipelineCurrentStage = pipelineCurrentStage;
        this.pipelineStartTime = pipelineStartTime;
        this.pipelineEndTime = pipelineEndTime;
        this.pipelineStatus = pipelineStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPipelineDefinition() {
        return pipelineDefinition;
    }

    public String getPipelineState() {
        return pipelineState;
    }

    public int getPipelineCurrentStage() {
        return pipelineCurrentStage;
    }

    public StepEntity getPipelineStartTime() {
        return pipelineStartTime;
    }

    public StepEntity getPipelineEndTime() {
        return pipelineEndTime;
    }

    public String getPipelineStatus() {
        return pipelineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPipelineInstance that = (StepPipelineInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pipelineDefinition, that.pipelineDefinition) && Objects.equals(pipelineState, that.pipelineState) && pipelineCurrentStage == that.pipelineCurrentStage && Objects.equals(pipelineStartTime, that.pipelineStartTime) && Objects.equals(pipelineEndTime, that.pipelineEndTime) && Objects.equals(pipelineStatus, that.pipelineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pipelineDefinition, pipelineState, pipelineCurrentStage, pipelineStartTime, pipelineEndTime, pipelineStatus);
    }

    @Override
    public String toString() {
        return "StepPipelineInstance{" + "id=" + id + "name=" + name + "pipelineDefinition=" + pipelineDefinition + "pipelineState=" + pipelineState + "pipelineCurrentStage=" + pipelineCurrentStage + "pipelineStartTime=" + pipelineStartTime + "pipelineEndTime=" + pipelineEndTime + "pipelineStatus=" + pipelineStatus + "}";
    }
}