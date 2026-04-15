package com.minicad.step.model;

import java.util.List;

/**
 * Resolved THRESHOLD_INSTANCE.
 * A threshold instance entity.
 *
 * @param id STEP instance id
 * @param name threshold instance name
 * @param thresholdDefinition threshold variance definition reference
 * @param thresholdState threshold variance state (normal/warning/critical)
 * @param thresholdCurrentValue threshold variance current value
 * @param thresholdViolations threshold variance violation count
 * @param thresholdStatus threshold variance status
 */
public record StepThresholdInstance(
    int id,
    String name,
    StepEntity thresholdDefinition,
    String thresholdState,
    double thresholdCurrentValue,
    int thresholdViolations,
    String thresholdStatus) implements StepEntity {
}