package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepBufferDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String bufferType;
    private final int bufferCapacity;
    private final String bufferPolicy;
    private final String bufferStatus;

    public StepBufferDefinition(int id, String name, String bufferType, int bufferCapacity, String bufferPolicy, String bufferStatus) {
        this.id = id;
        this.name = name;
        this.bufferType = bufferType;
        this.bufferCapacity = bufferCapacity;
        this.bufferPolicy = bufferPolicy;
        this.bufferStatus = bufferStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBufferDefinition that = (StepBufferDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(bufferType, that.bufferType) && bufferCapacity == that.bufferCapacity && Objects.equals(bufferPolicy, that.bufferPolicy) && Objects.equals(bufferStatus, that.bufferStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bufferType, bufferCapacity, bufferPolicy, bufferStatus);
    }

    @Override
    public String toString() {
        return "StepBufferDefinition{" + "id=" + id + "name=" + name + "bufferType=" + bufferType + "bufferCapacity=" + bufferCapacity + "bufferPolicy=" + bufferPolicy + "bufferStatus=" + bufferStatus + "}";
    }
}