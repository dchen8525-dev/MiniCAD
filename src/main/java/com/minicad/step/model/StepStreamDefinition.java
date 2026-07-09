package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STREAM_DEFINITION.
 * A stream definition entity.
 *
 * @param id STEP instance id
 * @param name stream name
 * @param streamType stream variance type
 * @param streamDirection stream variance direction
 * @param streamFormat stream variance format
 * @param streamBufferSize stream variance buffer size
 * @param streamStatus stream variance status
 */
/**
 * Resolved STREAM_DEFINITION.
 * A stream definition entity.
 *
 * @param id STEP instance id
 * @param name stream name
 * @param streamType stream variance type
 * @param streamDirection stream variance direction
 * @param streamFormat stream variance format
 * @param streamBufferSize stream variance buffer size
 * @param streamStatus stream variance status
 */
public final class StepStreamDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String streamType;
    private final String streamDirection;
    private final String streamFormat;
    private final int streamBufferSize;
    private final String streamStatus;

    public StepStreamDefinition(int id, String name, String streamType, String streamDirection, String streamFormat, int streamBufferSize, String streamStatus) {
        this.id = id;
        this.name = name;
        this.streamType = streamType;
        this.streamDirection = streamDirection;
        this.streamFormat = streamFormat;
        this.streamBufferSize = streamBufferSize;
        this.streamStatus = streamStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStreamType() {
        return streamType;
    }

    public String getStreamDirection() {
        return streamDirection;
    }

    public String getStreamFormat() {
        return streamFormat;
    }

    public int getStreamBufferSize() {
        return streamBufferSize;
    }

    public String getStreamStatus() {
        return streamStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStreamDefinition that = (StepStreamDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(streamType, that.streamType) && Objects.equals(streamDirection, that.streamDirection) && Objects.equals(streamFormat, that.streamFormat) && streamBufferSize == that.streamBufferSize && Objects.equals(streamStatus, that.streamStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, streamType, streamDirection, streamFormat, streamBufferSize, streamStatus);
    }

    @Override
    public String toString() {
        return "StepStreamDefinition{" + "id=" + id + "name=" + name + "streamType=" + streamType + "streamDirection=" + streamDirection + "streamFormat=" + streamFormat + "streamBufferSize=" + streamBufferSize + "streamStatus=" + streamStatus + "}";
    }
}