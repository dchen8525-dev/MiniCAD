package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CALLBACK_DEFINITION.
 * A callback definition entity.
 *
 * @param id STEP instance id
 * @param name callback name
 * @param callbackType callback variance type
 * @param callbackFunction callback variance function reference
 * @param callbackParameters callback variance parameters
 * @param callbackAsync callback variance async flag
 * @param callbackStatus callback variance status
 */
public record StepCallbackDefinition(
    int id,
    String name,
    String callbackType,
    StepEntity callbackFunction,
    List<String> callbackParameters,
    boolean callbackAsync,
    String callbackStatus) implements StepEntity {
}