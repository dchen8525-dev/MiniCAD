package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MESSAGE_INSTANCE.
 * A message instance entity.
 *
 * @param id STEP instance id
 * @param name message instance name
 * @param messageDefinition message variance definition reference
 * @param messageSource message variance source reference
 * @param messageDestination message variance destination reference
 * @param messagePayload message variance payload content
 * @param messageSentTime message variance sent time
 * @param messageStatus message variance status
 */
public record StepMessageInstance(
    int id,
    String name,
    StepEntity messageDefinition,
    StepEntity messageSource,
    StepEntity messageDestination,
    String messagePayload,
    StepEntity messageSentTime,
    String messageStatus) implements StepEntity {
}