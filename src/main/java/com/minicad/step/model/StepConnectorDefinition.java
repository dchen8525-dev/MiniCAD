package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CONNECTOR_DEFINITION.
 * A connector definition entity.
 *
 * @param id STEP instance id
 * @param name connector name
 * @param connectorType connector variance type
 * @param connectorGeometry connector variance geometry reference
 * @param connectorPins connector variance pin definitions
 * @param connectorRating connector variance electrical rating
 * @param connectorStatus connector variance status
 */
public record StepConnectorDefinition(
    int id,
    String name,
    String connectorType,
    StepEntity connectorGeometry,
    List<StepEntity> connectorPins,
    String connectorRating,
    String connectorStatus) implements StepEntity {
}