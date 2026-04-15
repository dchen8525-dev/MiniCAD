package com.minicad.step.model;

import java.util.List;

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
public record StepStageDefinition(
    int id,
    String name,
    String stageType,
    int stageSequence,
    List<StepEntity> stageTasks,
    List<StepEntity> stageDependencies,
    String stageStatus) implements StepEntity {
}