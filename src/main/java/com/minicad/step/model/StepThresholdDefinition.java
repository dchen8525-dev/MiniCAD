package com.minicad.step.model;

import java.util.List;

/**
 * Resolved THRESHOLD_DEFINITION.
 * A threshold definition entity.
 *
 * @param id STEP instance id
 * @param name threshold name
 * @param thresholdType threshold variance type
 * @param thresholdValue threshold variance value
 * @param thresholdTolerance threshold variance tolerance
 * @param thresholdActions threshold variance actions when exceeded
 * @param thresholdStatus threshold variance status
 */
public record StepThresholdDefinition(
    int id,
    String name,
    String thresholdType,
    double thresholdValue,
    double thresholdTolerance,
    List<String> thresholdActions,
    String thresholdStatus) implements StepEntity {
}