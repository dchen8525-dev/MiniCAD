package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepQueueInstance extends AbstractStepEntity {
    private final StepEntity queueDefinition;
    private final String queueState;
    private final int queueSize;
    private final int queuePending;
    private final String queueStatus;

    public StepQueueInstance(int id, String name, StepEntity queueDefinition, String queueState, int queueSize, int queuePending, String queueStatus) {
        super(id, name);
        this.queueDefinition = queueDefinition;
        this.queueState = queueState;
        this.queueSize = queueSize;
        this.queuePending = queuePending;
        this.queueStatus = queueStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "queueDefinition", queueDefinition,
                "queueState", queueState, "queueSize", queueSize, "queuePending", queuePending,
                "queueStatus", queueStatus);
    }
}
