package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ACTION_CHAIN_INSTANCE.
 * An action chain instance entity.
 *
 * @param id STEP instance id
 * @param name action chain instance name
 * @param chainDefinition chain variance definition reference
 * @param chainState chain variance state
 * @param chainCurrentAction chain variance current action
 * @param chainCompletedActions chain variance completed action count
 * @param chainStatus chain variance status
 */
public record StepActionChainInstance(
    int id,
    String name,
    StepEntity chainDefinition,
    String chainState,
    int chainCurrentAction,
    int chainCompletedActions,
    String chainStatus) implements StepEntity {
}