package com.minicad.step.model;

import java.util.List;

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
public record StepStageInstance(
    int id,
    String name,
    StepEntity stageDefinition,
    String stageState,
    StepEntity stageStartTime,
    StepEntity stageEndTime,
    double stageProgress,
    String stageStatus) implements StepEntity {
}