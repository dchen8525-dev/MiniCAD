package com.minicad.step.model;

import java.util.List;

/**
 * Resolved WAIT_DEFINITION.
 * A wait definition entity.
 *
 * @param id STEP instance id
 * @param name wait name
 * @param waitType wait variance type
 * @param waitCondition wait variance condition
 * @param waitTimeout wait variance timeout
 * @param waitAction wait variance action on timeout
 * @param waitStatus wait variance status
 */
public record StepWaitDefinition(
    int id,
    String name,
    String waitType,
    String waitCondition,
    int waitTimeout,
    StepEntity waitAction,
    String waitStatus) implements StepEntity {
}