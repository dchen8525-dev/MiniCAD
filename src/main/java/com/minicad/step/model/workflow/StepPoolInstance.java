package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved POOL_INSTANCE.
 * A pool instance entity.
 *
 * @param id STEP instance id
 * @param name pool instance name
 * @param poolDefinition pool variance definition reference
 * @param poolState pool variance state
 * @param poolUsed pool variance used capacity
 * @param poolAvailable pool variance available capacity
 * @param poolStatus pool variance status
 */
public record StepPoolInstance(
    int id,
    String name,
    StepEntity poolDefinition,
    String poolState,
    double poolUsed,
    double poolAvailable,
    String poolStatus) implements StepEntity {
}