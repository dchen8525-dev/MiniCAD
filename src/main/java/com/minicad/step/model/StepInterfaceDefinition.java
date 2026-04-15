package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INTERFACE_DEFINITION.
 * An interface definition entity.
 *
 * @param id STEP instance id
 * @param name interface name
 * @param interfaceType interface variance type
 * @param interfaceProtocol interface variance protocol
 * @param interfaceParameters interface variance parameters
 * @param interfaceConstraints interface variance constraints
 * @param interfaceStatus interface variance status
 */
public record StepInterfaceDefinition(
    int id,
    String name,
    String interfaceType,
    String interfaceProtocol,
    List<String> interfaceParameters,
    List<String> interfaceConstraints,
    String interfaceStatus) implements StepEntity {
}