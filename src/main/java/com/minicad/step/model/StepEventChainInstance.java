package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EVENT_CHAIN_INSTANCE.
 * An event chain instance entity.
 *
 * @param id STEP instance id
 * @param name event chain instance name
 * @param chainDefinition chain variance definition reference
 * @param chainState chain variance state
 * @param chainCurrentEvent chain variance current event position
 * @param chainCompletedEvents chain variance completed event count
 * @param chainStatus chain variance status
 */
public record StepEventChainInstance(
    int id,
    String name,
    StepEntity chainDefinition,
    String chainState,
    int chainCurrentEvent,
    int chainCompletedEvents,
    String chainStatus) implements StepEntity {
}