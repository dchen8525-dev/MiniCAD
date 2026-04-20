package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved EVENT_CHAIN_DEFINITION.
 * An event chain definition entity.
 *
 * @param id STEP instance id
 * @param name event chain name
 * @param chainType chain variance type
 * @param chainEvents chain variance event definitions
 * @param chainOrder chain variance ordering
 * @param chainParallel chain variance parallel flag
 * @param chainStatus chain variance status
 */
public record StepEventChainDefinition(
    int id,
    String name,
    String chainType,
    List<StepEntity> chainEvents,
    String chainOrder,
    boolean chainParallel,
    String chainStatus) implements StepEntity {
}