package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepFrameDefinition extends AbstractStepEntity {
    private final String frameType;
    private final String frameFormat;
    private final int frameSize;
    private final double frameDuration;
    private final String frameStatus;

    public StepFrameDefinition(int id, String name, String frameType, String frameFormat, int frameSize, double frameDuration, String frameStatus) {
        super(id, name);
        this.frameType = frameType;
        this.frameFormat = frameFormat;
        this.frameSize = frameSize;
        this.frameDuration = frameDuration;
        this.frameStatus = frameStatus;
    }

    public String getFrameType() {
        return frameType;
    }

    public String getFrameFormat() {
        return frameFormat;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public double getFrameDuration() {
        return frameDuration;
    }

    public String getFrameStatus() {
        return frameStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("frameType", frameType);
        state.put("frameFormat", frameFormat);
        state.put("frameSize", frameSize);
        state.put("frameDuration", frameDuration);
        state.put("frameStatus", frameStatus);
        return state;
    }
}
