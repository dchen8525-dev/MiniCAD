package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved QUEUE_INSTANCE.
 * A queue instance entity.
 *
 * @param id STEP instance id
 * @param name queue instance name
 * @param queueDefinition queue variance definition reference
 * @param queueState queue variance state
 * @param queueSize queue variance current size
 * @param queuePending queue variance pending count
 * @param queueStatus queue variance status
 */
/**
 * Resolved QUEUE_INSTANCE.
 * A queue instance entity.
 *
 * @param id STEP instance id
 * @param name queue instance name
 * @param queueDefinition queue variance definition reference
 * @param queueState queue variance state
 * @param queueSize queue variance current size
 * @param queuePending queue variance pending count
 * @param queueStatus queue variance status
 */
public final class StepQueueInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity queueDefinition;
    private final String queueState;
    private final int queueSize;
    private final int queuePending;
    private final String queueStatus;

    public StepQueueInstance(int id, String name, StepEntity queueDefinition, String queueState, int queueSize, int queuePending, String queueStatus) {
        this.id = id;
        this.name = name;
        this.queueDefinition = queueDefinition;
        this.queueState = queueState;
        this.queueSize = queueSize;
        this.queuePending = queuePending;
        this.queueStatus = queueStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getQueueDefinition() {
        return queueDefinition;
    }

    public String getQueueState() {
        return queueState;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public int getQueuePending() {
        return queuePending;
    }

    public String getQueueStatus() {
        return queueStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQueueInstance that = (StepQueueInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(queueDefinition, that.queueDefinition) && Objects.equals(queueState, that.queueState) && queueSize == that.queueSize && queuePending == that.queuePending && Objects.equals(queueStatus, that.queueStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, queueDefinition, queueState, queueSize, queuePending, queueStatus);
    }

    @Override
    public String toString() {
        return "StepQueueInstance{" + "id=" + id + "name=" + name + "queueDefinition=" + queueDefinition + "queueState=" + queueState + "queueSize=" + queueSize + "queuePending=" + queuePending + "queueStatus=" + queueStatus + "}";
    }
}