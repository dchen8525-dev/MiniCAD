package com.minicad.step.model;

import java.util.List;

/**
 * Resolved NODE_INSTANCE.
 * A node instance entity.
 *
 * @param id STEP instance id
 * @param name node instance name
 * @param nodeDefinition node variance definition reference
 * @param nodeState node variance state
 * @param nodeAddress node variance address/identifier
 * @param nodeLoad node variance load percentage
 * @param nodeStatus node variance status
 */
public record StepNodeInstance(
    int id,
    String name,
    StepEntity nodeDefinition,
    String nodeState,
    String nodeAddress,
    double nodeLoad,
    String nodeStatus) implements StepEntity {
}