package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONNECTOR_INSTANCE.
 * A connector instance entity.
 *
 * @param id STEP instance id
 * @param name connector instance name
 * @param connectorDefinition connector variance definition reference
 * @param connectorLocation connector variance location reference
 * @param connectorState connector variance state
 * @param connectorPinStates connector variance pin states
 * @param connectorStatus connector variance status
 */
public record StepConnectorInstance(
    int id,
    String name,
    StepEntity connectorDefinition,
    StepEntity connectorLocation,
    String connectorState,
    List<String> connectorPinStates,
    String connectorStatus) implements StepEntity {
}