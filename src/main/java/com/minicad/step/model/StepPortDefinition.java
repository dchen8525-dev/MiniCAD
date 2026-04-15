package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PORT_DEFINITION.
 * A port definition entity.
 *
 * @param id STEP instance id
 * @param name port name
 * @param portType port variance type
 * @param portDirection port variance direction (input/output/bidirectional)
 * @param portDataType port variance data type
 * @param portProtocol port variance protocol
 * @param portStatus port variance status
 */
public record StepPortDefinition(
    int id,
    String name,
    String portType,
    String portDirection,
    String portDataType,
    String portProtocol,
    String portStatus) implements StepEntity {
}