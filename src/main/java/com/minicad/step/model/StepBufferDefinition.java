package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved BUFFER_DEFINITION.
 * A buffer definition entity.
 *
 * @param id STEP instance id
 * @param name buffer name
 * @param bufferType buffer variance type
 * @param bufferCapacity buffer variance capacity
 * @param bufferPolicy buffer variance policy
 * @param bufferStatus buffer variance status
 */
public final class StepBufferDefinition extends AbstractStepEntity {
    private final String bufferType;
    private final int bufferCapacity;
    private final String bufferPolicy;
    private final String bufferStatus;

    public StepBufferDefinition(int id, String name, String bufferType, int bufferCapacity, String bufferPolicy, String bufferStatus) {
        super(id, name);
        this.bufferType = bufferType;
        this.bufferCapacity = bufferCapacity;
        this.bufferPolicy = bufferPolicy;
        this.bufferStatus = bufferStatus;
    }

    public String getBufferType() {
        return bufferType;
    }

    public int getBufferCapacity() {
        return bufferCapacity;
    }

    public String getBufferPolicy() {
        return bufferPolicy;
    }

    public String getBufferStatus() {
        return bufferStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "bufferType", bufferType, "bufferCapacity", bufferCapacity,
                "bufferPolicy", bufferPolicy, "bufferStatus", bufferStatus);
    }
}
