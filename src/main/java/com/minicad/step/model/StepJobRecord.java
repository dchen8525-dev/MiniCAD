package com.minicad.step.model;

import java.util.List;

/**
 * Resolved JOB_RECORD.
 * A job record entity.
 *
 * @param id STEP instance id
 * @param name job name
 * @param jobType job variance type
 * @param jobTarget job variance target reference
 * @param jobStartTime job variance start time
 * @param jobEndTime job variance end time
 * @param jobResult job variance result
 * @param jobDetails job variance details
 * @param jobStatus job variance status
 */
public record StepJobRecord(
    int id,
    String name,
    String jobType,
    StepEntity jobTarget,
    StepEntity jobStartTime,
    StepEntity jobEndTime,
    String jobResult,
    List<String> jobDetails,
    String jobStatus) implements StepEntity {
}