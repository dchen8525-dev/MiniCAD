package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INTERFACE_INSTANCE.
 * An interface instance entity.
 *
 * @param id STEP instance id
 * @param name interface instance name
 * @param interfaceDefinition interface variance definition reference
 * @param interfaceLocation interface variance location reference
 * @param interfaceState interface variance state
 * @param interfaceConnections interface variance connections
 * @param interfaceStatus interface variance status
 */
public record StepInterfaceInstance(
    int id,
    String name,
    StepEntity interfaceDefinition,
    StepEntity interfaceLocation,
    String interfaceState,
    List<StepEntity> interfaceConnections,
    String interfaceStatus) implements StepEntity {
}