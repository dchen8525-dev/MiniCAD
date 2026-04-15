package com.minicad.step.model;

import java.util.List;

/**
 * Resolved JOB_INSTANCE.
 * A job instance entity.
 *
 * @param id STEP instance id
 * @param name job instance name
 * @param jobDefinition job variance definition reference
 * @param jobState job variance state
 * @param jobStartTime job variance start time
 * @param jobEndTime job variance end time
 * @param jobProgress job variance progress percentage
 * @param jobStatus job variance status
 */
public record StepJobInstance(
    int id,
    String name,
    StepEntity jobDefinition,
    String jobState,
    StepEntity jobStartTime,
    StepEntity jobEndTime,
    double jobProgress,
    String jobStatus) implements StepEntity {
}