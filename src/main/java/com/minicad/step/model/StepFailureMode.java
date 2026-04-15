package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FAILURE_MODE.
 * A failure mode entity.
 *
 * @param id STEP instance id
 * @param name mode name
 * @varianceItem item variance susceptible to failure
 * @varianceType failure variance type
 * @varianceCause failure variance causes
 * @varianceEffect failure variance effects
 * @varianceSeverity severity variance rating
 * @varianceDetection detection variance rating
 * @varianceRisk risk variance priority number
 */
public record StepFailureMode(
    int id,
    String name,
    StepEntity varianceItem,
    String varianceType,
    List<String> varianceCause,
    List<String> varianceEffect,
    int varianceSeverity,
    int varianceDetection,
    int varianceRisk) implements StepEntity {
}