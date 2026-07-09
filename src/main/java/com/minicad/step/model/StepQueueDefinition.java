package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved QUEUE_DEFINITION.
 * A queue definition entity.
 *
 * @param id STEP instance id
 * @param name queue name
 * @param queueType queue variance type
 * @param queueCapacity queue variance capacity
 * @param queuePolicy queue variance policy
 * @param queuePriority queue variance priority support
 * @param queueStatus queue variance status
 */
/**
 * Resolved QUEUE_DEFINITION.
 * A queue definition entity.
 *
 * @param id STEP instance id
 * @param name queue name
 * @param queueType queue variance type
 * @param queueCapacity queue variance capacity
 * @param queuePolicy queue variance policy
 * @param queuePriority queue variance priority support
 * @param queueStatus queue variance status
 */
public final class StepQueueDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String queueType;
    private final int queueCapacity;
    private final String queuePolicy;
    private final boolean queuePriority;
    private final String queueStatus;

    public StepQueueDefinition(int id, String name, String queueType, int queueCapacity, String queuePolicy, boolean queuePriority, String queueStatus) {
        this.id = id;
        this.name = name;
        this.queueType = queueType;
        this.queueCapacity = queueCapacity;
        this.queuePolicy = queuePolicy;
        this.queuePriority = queuePriority;
        this.queueStatus = queueStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQueueType() {
        return queueType;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public String getQueuePolicy() {
        return queuePolicy;
    }

    public boolean isQueuePriority() {
        return queuePriority;
    }

    public String getQueueStatus() {
        return queueStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQueueDefinition that = (StepQueueDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(queueType, that.queueType) && queueCapacity == that.queueCapacity && Objects.equals(queuePolicy, that.queuePolicy) && queuePriority == that.queuePriority && Objects.equals(queueStatus, that.queueStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, queueType, queueCapacity, queuePolicy, queuePriority, queueStatus);
    }

    @Override
    public String toString() {
        return "StepQueueDefinition{" + "id=" + id + "name=" + name + "queueType=" + queueType + "queueCapacity=" + queueCapacity + "queuePolicy=" + queuePolicy + "queuePriority=" + queuePriority + "queueStatus=" + queueStatus + "}";
    }
}