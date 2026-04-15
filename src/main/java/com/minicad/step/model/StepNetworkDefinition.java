package com.minicad.step.model;

import java.util.List;

/**
 * Resolved NETWORK_DEFINITION.
 * A network definition entity.
 *
 * @param id STEP instance id
 * @param name network name
 * @param networkType network variance type
 * @param networkTopology network variance topology
 * @param networkNodes network variance node definitions
 * @param networkLinks network variance link definitions
 * @param networkStatus network variance status
 */
public record StepNetworkDefinition(
    int id,
    String name,
    String networkType,
    String networkTopology,
    List<StepEntity> networkNodes,
    List<StepEntity> networkLinks,
    String networkStatus) implements StepEntity {
}