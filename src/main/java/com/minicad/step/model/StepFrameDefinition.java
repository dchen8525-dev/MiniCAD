package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFrameDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String frameType;
    private final String frameFormat;
    private final int frameSize;
    private final double frameDuration;
    private final String frameStatus;

    public StepFrameDefinition(int id, String name, String frameType, String frameFormat, int frameSize, double frameDuration, String frameStatus) {
        this.id = id;
        this.name = name;
        this.frameType = frameType;
        this.frameFormat = frameFormat;
        this.frameSize = frameSize;
        this.frameDuration = frameDuration;
        this.frameStatus = frameStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFrameDefinition that = (StepFrameDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(frameType, that.frameType) && Objects.equals(frameFormat, that.frameFormat) && frameSize == that.frameSize && frameDuration == that.frameDuration && Objects.equals(frameStatus, that.frameStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, frameType, frameFormat, frameSize, frameDuration, frameStatus);
    }

    @Override
    public String toString() {
        return "StepFrameDefinition{" + "id=" + id + "name=" + name + "frameType=" + frameType + "frameFormat=" + frameFormat + "frameSize=" + frameSize + "frameDuration=" + frameDuration + "frameStatus=" + frameStatus + "}";
    }
}