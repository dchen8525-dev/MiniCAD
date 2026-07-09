package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFrameInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity frameDefinition;
    private final long frameNumber;
    private final String frameData;
    private final StepEntity frameTimestamp;
    private final String frameStatus;

    public StepFrameInstance(int id, String name, StepEntity frameDefinition, long frameNumber, String frameData, StepEntity frameTimestamp, String frameStatus) {
        this.id = id;
        this.name = name;
        this.frameDefinition = frameDefinition;
        this.frameNumber = frameNumber;
        this.frameData = frameData;
        this.frameTimestamp = frameTimestamp;
        this.frameStatus = frameStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFrameInstance that = (StepFrameInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(frameDefinition, that.frameDefinition) && frameNumber == that.frameNumber && Objects.equals(frameData, that.frameData) && Objects.equals(frameTimestamp, that.frameTimestamp) && Objects.equals(frameStatus, that.frameStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, frameDefinition, frameNumber, frameData, frameTimestamp, frameStatus);
    }

    @Override
    public String toString() {
        return "StepFrameInstance{" + "id=" + id + "name=" + name + "frameDefinition=" + frameDefinition + "frameNumber=" + frameNumber + "frameData=" + frameData + "frameTimestamp=" + frameTimestamp + "frameStatus=" + frameStatus + "}";
    }
}