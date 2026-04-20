package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved NODE_DEFINITION.
 * A node definition entity.
 *
 * @param id STEP instance id
 * @param name node name
 * @param nodeType node variance type
 * @param nodeLocation node variance location reference
 * @param nodeCapabilities node variance capabilities
 * @param nodeConnections node variance connections
 * @param nodeStatus node variance status
 */
public record StepNodeDefinition(
    int id,
    String name,
    String nodeType,
    StepEntity nodeLocation,
    List<String> nodeCapabilities,
    List<StepEntity> nodeConnections,
    String nodeStatus) implements StepEntity {
}