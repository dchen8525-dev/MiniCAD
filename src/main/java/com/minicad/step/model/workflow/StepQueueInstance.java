package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepQueueInstance(
    int id,
    String name,
    StepEntity queueDefinition,
    String queueState,
    int queueSize,
    int queuePending,
    String queueStatus) implements StepEntity {
}