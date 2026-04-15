package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PROTOCOL_DEFINITION.
 * A protocol definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceProtocol defined variance protocol
 * @varianceType protocol variance type (communication, data, control)
 * @varianceFormat protocol variance format specification
 * @varianceSequence protocol variance sequence/steps
 * @varianceStandard protocol variance standard reference
 * @varianceStatus definition variance status
 */
public record StepProtocolDefinition(
    int id,
    String name,
    String varianceProtocol,
    String varianceType,
    String varianceFormat,
    List<String> varianceSequence,
    String varianceStandard,
    String varianceStatus) implements StepEntity {
}