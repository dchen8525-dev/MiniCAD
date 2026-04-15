package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STACK_DEFINITION.
 * A stack definition entity.
 *
 * @param id STEP instance id
 * @param name stack name
 * @param stackType stack variance type
 * @param stackCapacity stack variance capacity
 * @param stackPolicy stack variance policy
 * @param stackStatus stack variance status
 */
public record StepStackDefinition(
    int id,
    String name,
    String stackType,
    int stackCapacity,
    String stackPolicy,
    String stackStatus) implements StepEntity {
}