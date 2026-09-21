package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepStreamDefinition extends AbstractStepEntity {
    private final String streamType;
    private final String streamDirection;
    private final String streamFormat;
    private final int streamBufferSize;
    private final String streamStatus;

    public StepStreamDefinition(int id, String name, String streamType, String streamDirection, String streamFormat, int streamBufferSize, String streamStatus) {
        super(id, name);
        this.streamType = streamType;
        this.streamDirection = streamDirection;
        this.streamFormat = streamFormat;
        this.streamBufferSize = streamBufferSize;
        this.streamStatus = streamStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("streamType", streamType);
        state.put("streamDirection", streamDirection);
        state.put("streamFormat", streamFormat);
        state.put("streamBufferSize", streamBufferSize);
        state.put("streamStatus", streamStatus);
        return state;
    }
}
