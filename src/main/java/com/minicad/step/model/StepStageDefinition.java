package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STAGE_DEFINITION.
 * A stage definition entity.
 *
 * @param id STEP instance id
 * @param name stage name
 * @param stageType stage variance type
 * @param stageSequence stage variance sequence number
 * @param stageTasks stage variance task definitions
 * @param stageDependencies stage variance dependencies
 * @param stageStatus stage variance status
 */
/**
 * Resolved STAGE_DEFINITION.
 * A stage definition entity.
 *
 * @param id STEP instance id
 * @param name stage name
 * @param stageType stage variance type
 * @param stageSequence stage variance sequence number
 * @param stageTasks stage variance task definitions
 * @param stageDependencies stage variance dependencies
 * @param stageStatus stage variance status
 */
public final class StepStageDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String stageType;
    private final int stageSequence;
    private final List<StepEntity> stageTasks;
    private final List<StepEntity> stageDependencies;
    private final String stageStatus;

    public StepStageDefinition(int id, String name, String stageType, int stageSequence, List<StepEntity> stageTasks, List<StepEntity> stageDependencies, String stageStatus) {
        this.id = id;
        this.name = name;
        this.stageType = stageType;
        this.stageSequence = stageSequence;
        this.stageTasks = stageTasks == null ? null : java.util.List.copyOf(stageTasks);
        this.stageDependencies = stageDependencies == null ? null : java.util.List.copyOf(stageDependencies);
        this.stageStatus = stageStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStageType() {
        return stageType;
    }

    public int getStageSequence() {
        return stageSequence;
    }

    public List<StepEntity> getStageTasks() {
        return stageTasks;
    }

    public List<StepEntity> getStageDependencies() {
        return stageDependencies;
    }

    public String getStageStatus() {
        return stageStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStageDefinition that = (StepStageDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(stageType, that.stageType) && stageSequence == that.stageSequence && Objects.equals(stageTasks, that.stageTasks) && Objects.equals(stageDependencies, that.stageDependencies) && Objects.equals(stageStatus, that.stageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stageType, stageSequence, stageTasks, stageDependencies, stageStatus);
    }

    @Override
    public String toString() {
        return "StepStageDefinition{" + "id=" + id + "name=" + name + "stageType=" + stageType + "stageSequence=" + stageSequence + "stageTasks=" + stageTasks + "stageDependencies=" + stageDependencies + "stageStatus=" + stageStatus + "}";
    }
}