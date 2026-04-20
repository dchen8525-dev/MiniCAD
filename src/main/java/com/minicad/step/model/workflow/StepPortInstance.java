package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PORT_INSTANCE.
 * A port instance entity.
 *
 * @param id STEP instance id
 * @param name port instance name
 * @param portDefinition port variance definition reference
 * @param portState port variance state
 * @param portValue port variance current value
 * @param portConnections port variance connections
 * @param portStatus port variance status
 */
public record StepPortInstance(
    int id,
    String name,
    StepEntity portDefinition,
    String portState,
    String portValue,
    List<StepEntity> portConnections,
    String portStatus) implements StepEntity {
}