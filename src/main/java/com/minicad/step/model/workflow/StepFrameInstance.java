package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FRAME_INSTANCE.
 * A frame instance entity.
 *
 * @param id STEP instance id
 * @param name frame instance name
 * @param frameDefinition frame variance definition reference
 * @param frameNumber frame variance frame number
 * @param frameData frame variance data content
 * @param frameTimestamp frame variance timestamp
 * @param frameStatus frame variance status
 */
public record StepFrameInstance(
    int id,
    String name,
    StepEntity frameDefinition,
    long frameNumber,
    String frameData,
    StepEntity frameTimestamp,
    String frameStatus) implements StepEntity {
}