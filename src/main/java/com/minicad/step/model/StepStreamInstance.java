package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STREAM_INSTANCE.
 * A stream instance entity.
 *
 * @param id STEP instance id
 * @param name stream instance name
 * @param streamDefinition stream variance definition reference
 * @param streamState stream variance state
 * @param streamPosition stream variance position
 * @param streamRate stream variance rate
 * @param streamStatus stream variance status
 */
public final class StepStreamInstance extends AbstractStepEntity {
    private final StepEntity streamDefinition;
    private final String streamState;
    private final long streamPosition;
    private final double streamRate;
    private final String streamStatus;

    public StepStreamInstance(int id, String name, StepEntity streamDefinition, String streamState, long streamPosition, double streamRate, String streamStatus) {
        super(id, name);
        this.streamDefinition = streamDefinition;
        this.streamState = streamState;
        this.streamPosition = streamPosition;
        this.streamRate = streamRate;
        this.streamStatus = streamStatus;
    }

    public StepEntity getStreamDefinition() {
        return streamDefinition;
    }

    public String getStreamState() {
        return streamState;
    }

    public long getStreamPosition() {
        return streamPosition;
    }

    public double getStreamRate() {
        return streamRate;
    }

    public String getStreamStatus() {
        return streamStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("streamDefinition", streamDefinition);
        state.put("streamState", streamState);
        state.put("streamPosition", streamPosition);
        state.put("streamRate", streamRate);
        state.put("streamStatus", streamStatus);
        return state;
    }
}
