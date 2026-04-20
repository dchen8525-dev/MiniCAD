package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved POOL_DEFINITION.
 * A pool definition entity.
 *
 * @param id STEP instance id
 * @param name pool name
 * @param poolType pool variance type
 * @param poolResources pool variance resource definitions
 * @param poolAllocation pool variance allocation policy
 * @param poolCapacity pool variance capacity
 * @param poolStatus pool variance status
 */
public record StepPoolDefinition(
    int id,
    String name,
    String poolType,
    List<StepEntity> poolResources,
    String poolAllocation,
    double poolCapacity,
    String poolStatus) implements StepEntity {
}