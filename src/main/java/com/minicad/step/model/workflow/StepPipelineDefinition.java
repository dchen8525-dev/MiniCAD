package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PIPELINE_DEFINITION.
 * A pipeline definition entity.
 *
 * @param id STEP instance id
 * @param name pipeline name
 * @param pipelineType pipeline variance type
 * @param pipelineStages pipeline variance stage definitions
 * @param pipelineParallel pipeline variance parallel execution flag
 * @param pipelineTimeout pipeline variance timeout
 * @param pipelineStatus pipeline variance status
 */
public record StepPipelineDefinition(
    int id,
    String name,
    String pipelineType,
    List<StepEntity> pipelineStages,
    boolean pipelineParallel,
    int pipelineTimeout,
    String pipelineStatus) implements StepEntity {
}