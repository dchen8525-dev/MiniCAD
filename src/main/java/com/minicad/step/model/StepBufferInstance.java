package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved BUFFER_INSTANCE.
 * A buffer instance entity.
 *
 * @param id STEP instance id
 * @param name buffer instance name
 * @param bufferDefinition buffer variance definition reference
 * @param bufferState buffer variance state
 * @param bufferUsed buffer variance used bytes
 * @param bufferAvailable buffer variance available bytes
 * @param bufferStatus buffer variance status
 */
public final class StepBufferInstance extends AbstractStepEntity {
    private final StepEntity bufferDefinition;
    private final String bufferState;
    private final long bufferUsed;
    private final long bufferAvailable;
    private final String bufferStatus;

    public StepBufferInstance(int id, String name, StepEntity bufferDefinition, String bufferState, long bufferUsed, long bufferAvailable, String bufferStatus) {
        super(id, name);
        this.bufferDefinition = bufferDefinition;
        this.bufferState = bufferState;
        this.bufferUsed = bufferUsed;
        this.bufferAvailable = bufferAvailable;
        this.bufferStatus = bufferStatus;
    }

    public StepEntity getBufferDefinition() {
        return bufferDefinition;
    }

    public String getBufferState() {
        return bufferState;
    }

    public long getBufferUsed() {
        return bufferUsed;
    }

    public long getBufferAvailable() {
        return bufferAvailable;
    }

    public String getBufferStatus() {
        return bufferStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("bufferDefinition", bufferDefinition);
        state.put("bufferState", bufferState);
        state.put("bufferUsed", bufferUsed);
        state.put("bufferAvailable", bufferAvailable);
        state.put("bufferStatus", bufferStatus);
        return state;
    }
}
