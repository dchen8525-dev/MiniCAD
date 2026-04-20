package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved HANDLER_DEFINITION.
 * A handler definition entity.
 *
 * @param id STEP instance id
 * @param name handler name
 * @param handlerType handler variance type
 * @param handlerCondition handler variance trigger condition
 * @param handlerAction handler variance action reference
 * @param handlerPriority handler variance priority
 * @param handlerStatus handler variance status
 */
public record StepHandlerDefinition(
    int id,
    String name,
    String handlerType,
    String handlerCondition,
    StepEntity handlerAction,
    int handlerPriority,
    String handlerStatus) implements StepEntity {
}