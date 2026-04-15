package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STUDY_INSTANCE.
 * A study instance entity.
 *
 * @param id STEP instance id
 * @param name study instance name
 * @param studyDefinition study variance definition reference
 * @param studyState study variance state
 * @param studyStartTime study variance start time
 * @param studyEndTime study variance end time
 * @param studyResults study variance results
 * @param studyStatus study variance status
 */
public record StepStudyInstance(
    int id,
    String name,
    StepEntity studyDefinition,
    String studyState,
    StepEntity studyStartTime,
    StepEntity studyEndTime,
    List<StepEntity> studyResults,
    String studyStatus) implements StepEntity {
}