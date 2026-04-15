package com.minicad.step.model;

import java.util.List;

/**
 * Resolved HANDLER_INSTANCE.
 * A handler instance entity.
 *
 * @param id STEP instance id
 * @param name handler instance name
 * @param handlerDefinition handler variance definition reference
 * @param handlerState handler variance state
 * @param handlerTriggered handler variance triggered flag
 * @param handlerExecutionTime handler variance execution time
 * @param handlerStatus handler variance status
 */
public record StepHandlerInstance(
    int id,
    String name,
    StepEntity handlerDefinition,
    String handlerState,
    boolean handlerTriggered,
    StepEntity handlerExecutionTime,
    String handlerStatus) implements StepEntity {
}