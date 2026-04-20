package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved STATE_INSTANCE.
 * A state instance entity.
 *
 * @param id STEP instance id
 * @param name state instance name
 * @param stateDefinition state variance definition reference
 * @param stateActive state variance active flag
 * @param stateEntryTime state variance entry time
 * @param stateDuration state variance duration
 * @param stateStatus state variance status
 */
public record StepStateInstance(
    int id,
    String name,
    StepEntity stateDefinition,
    boolean stateActive,
    StepEntity stateEntryTime,
    int stateDuration,
    String stateStatus) implements StepEntity {
}