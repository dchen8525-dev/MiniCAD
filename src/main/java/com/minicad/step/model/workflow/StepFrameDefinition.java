package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FRAME_DEFINITION.
 * A frame definition entity.
 *
 * @param id STEP instance id
 * @param name frame name
 * @param frameType frame variance type
 * @param frameFormat frame variance format
 * @param frameSize frame variance size
 * @param frameDuration frame variance duration
 * @param frameStatus frame variance status
 */
public record StepFrameDefinition(
    int id,
    String name,
    String frameType,
    String frameFormat,
    int frameSize,
    double frameDuration,
    String frameStatus) implements StepEntity {
}