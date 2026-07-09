package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepStreamInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity streamDefinition;
    private final String streamState;
    private final long streamPosition;
    private final double streamRate;
    private final String streamStatus;

    public StepStreamInstance(int id, String name, StepEntity streamDefinition, String streamState, long streamPosition, double streamRate, String streamStatus) {
        this.id = id;
        this.name = name;
        this.streamDefinition = streamDefinition;
        this.streamState = streamState;
        this.streamPosition = streamPosition;
        this.streamRate = streamRate;
        this.streamStatus = streamStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStreamInstance that = (StepStreamInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(streamDefinition, that.streamDefinition) && Objects.equals(streamState, that.streamState) && streamPosition == that.streamPosition && streamRate == that.streamRate && Objects.equals(streamStatus, that.streamStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, streamDefinition, streamState, streamPosition, streamRate, streamStatus);
    }

    @Override
    public String toString() {
        return "StepStreamInstance{" + "id=" + id + "name=" + name + "streamDefinition=" + streamDefinition + "streamState=" + streamState + "streamPosition=" + streamPosition + "streamRate=" + streamRate + "streamStatus=" + streamStatus + "}";
    }
}