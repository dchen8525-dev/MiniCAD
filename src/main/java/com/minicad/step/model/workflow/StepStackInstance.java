package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved STACK_INSTANCE.
 * A stack instance entity.
 *
 * @param id STEP instance id
 * @param name stack instance name
 * @param stackDefinition stack variance definition reference
 * @param stackState stack variance state
 * @param stackDepth stack variance current depth
 * @param stackStatus stack variance status
 */
public record StepStackInstance(
    int id,
    String name,
    StepEntity stackDefinition,
    String stackState,
    int stackDepth,
    String stackStatus) implements StepEntity {
}