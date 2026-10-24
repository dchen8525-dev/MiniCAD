package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepFrameInstance extends AbstractStepEntity {
    private final StepEntity frameDefinition;
    private final long frameNumber;
    private final String frameData;
    private final StepEntity frameTimestamp;
    private final String frameStatus;

    public StepFrameInstance(int id, String name, StepEntity frameDefinition, long frameNumber, String frameData, StepEntity frameTimestamp, String frameStatus) {
        super(id, name);
        this.frameDefinition = frameDefinition;
        this.frameNumber = frameNumber;
        this.frameData = frameData;
        this.frameTimestamp = frameTimestamp;
        this.frameStatus = frameStatus;
    }

    public StepEntity getFrameDefinition() {
        return frameDefinition;
    }

    public long getFrameNumber() {
        return frameNumber;
    }

    public String getFrameData() {
        return frameData;
    }

    public StepEntity getFrameTimestamp() {
        return frameTimestamp;
    }

    public String getFrameStatus() {
        return frameStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "frameDefinition", frameDefinition,
                "frameNumber", frameNumber, "frameData", frameData, "frameTimestamp", frameTimestamp,
                "frameStatus", frameStatus);
    }
}
