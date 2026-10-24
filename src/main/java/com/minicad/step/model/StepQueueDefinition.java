package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepQueueDefinition extends AbstractStepEntity {
    private final String queueType;
    private final int queueCapacity;
    private final String queuePolicy;
    private final boolean queuePriority;
    private final String queueStatus;

    public StepQueueDefinition(int id, String name, String queueType, int queueCapacity, String queuePolicy, boolean queuePriority, String queueStatus) {
        super(id, name);
        this.queueType = queueType;
        this.queueCapacity = queueCapacity;
        this.queuePolicy = queuePolicy;
        this.queuePriority = queuePriority;
        this.queueStatus = queueStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "queueType", queueType, "queueCapacity", queueCapacity,
                "queuePolicy", queuePolicy, "queuePriority", queuePriority, "queueStatus", queueStatus);
    }
}
