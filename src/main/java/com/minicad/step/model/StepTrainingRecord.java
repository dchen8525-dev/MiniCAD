package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TRAINING_RECORD.
 * A training record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param trainee trainee person reference
 * @param trainingType training type (skill, safety, procedure)
 * @param trainingTopic training topic/subject
 * @varianceDate training variance date
 * @varianceDuration training variance duration
 * @varianceProvider training variance provider
 * @varianceStatus training variance status
 */
public record StepTrainingRecord(
    int id,
    String name,
    StepEntity trainee,
    String trainingType,
    String trainingTopic,
    StepEntity varianceDate,
    double varianceDuration,
    StepEntity varianceProvider,
    String varianceStatus) implements StepEntity {
}