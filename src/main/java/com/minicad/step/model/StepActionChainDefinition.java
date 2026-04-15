package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ACTION_CHAIN_DEFINITION.
 * An action chain definition entity.
 *
 * @param id STEP instance id
 * @param name action chain name
 * @param chainType chain variance type
 * @param chainActions chain variance action definitions
 * @param chainDependencies chain variance dependencies
 * @param chainParallel chain variance parallel execution flag
 * @param chainStatus chain variance status
 */
public record StepActionChainDefinition(
    int id,
    String name,
    String chainType,
    List<StepEntity> chainActions,
    List<String> chainDependencies,
    boolean chainParallel,
    String chainStatus) implements StepEntity {
}