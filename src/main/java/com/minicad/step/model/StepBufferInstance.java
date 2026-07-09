package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
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
public final class StepBufferInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity bufferDefinition;
    private final String bufferState;
    private final long bufferUsed;
    private final long bufferAvailable;
    private final String bufferStatus;

    public StepBufferInstance(int id, String name, StepEntity bufferDefinition, String bufferState, long bufferUsed, long bufferAvailable, String bufferStatus) {
        this.id = id;
        this.name = name;
        this.bufferDefinition = bufferDefinition;
        this.bufferState = bufferState;
        this.bufferUsed = bufferUsed;
        this.bufferAvailable = bufferAvailable;
        this.bufferStatus = bufferStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBufferInstance that = (StepBufferInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(bufferDefinition, that.bufferDefinition) && Objects.equals(bufferState, that.bufferState) && bufferUsed == that.bufferUsed && bufferAvailable == that.bufferAvailable && Objects.equals(bufferStatus, that.bufferStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bufferDefinition, bufferState, bufferUsed, bufferAvailable, bufferStatus);
    }

    @Override
    public String toString() {
        return "StepBufferInstance{" + "id=" + id + "name=" + name + "bufferDefinition=" + bufferDefinition + "bufferState=" + bufferState + "bufferUsed=" + bufferUsed + "bufferAvailable=" + bufferAvailable + "bufferStatus=" + bufferStatus + "}";
    }
}