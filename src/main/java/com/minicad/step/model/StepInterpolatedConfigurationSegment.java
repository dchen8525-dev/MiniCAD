package com.minicad.step.model;

/**
 * Resolved INTERPOLATED_CONFIGURATION_SEGMENT.
 * Interpolated configuration segment.
 */
public record StepInterpolatedConfigurationSegment(
    int id,
    String name,
    String description,
    StepEntity configuration) implements StepEntity {
}
