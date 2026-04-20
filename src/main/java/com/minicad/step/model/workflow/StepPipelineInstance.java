package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepPipelineInstance(
    int id,
    String name,
    StepEntity pipelineDefinition,
    String pipelineState,
    int pipelineCurrentStage,
    StepEntity pipelineStartTime,
    StepEntity pipelineEndTime,
    String pipelineStatus) implements StepEntity {
}