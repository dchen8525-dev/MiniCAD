package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MESSAGE_DEFINITION.
 * A message definition entity.
 *
 * @param id STEP instance id
 * @param name message name
 * @param messageType message variance type
 * @param messageFormat message variance format
 * @param messageFields message variance field definitions
 * @param messageStatus message variance status
 */
public record StepMessageDefinition(
    int id,
    String name,
    String messageType,
    String messageFormat,
    List<String> messageFields,
    String messageStatus) implements StepEntity {
}