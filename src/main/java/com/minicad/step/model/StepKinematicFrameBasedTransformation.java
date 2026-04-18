package com.minicad.step.model;

/**
 * Resolved KINEMATIC_FRAME_BASED_TRANSFORMATION.
 * A transformation defined by the relative positioning of kinematic frames.
 */
public record StepKinematicFrameBasedTransformation(
    int id,
    String name,
    String description,
    StepEntity sourceFrame,
    StepEntity targetFrame
) implements StepEntity {}
