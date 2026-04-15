package com.minicad.step.model;

import java.util.List;

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
public record StepQueueDefinition(
    int id,
    String name,
    String queueType,
    int queueCapacity,
    String queuePolicy,
    boolean queuePriority,
    String queueStatus) implements StepEntity {
}