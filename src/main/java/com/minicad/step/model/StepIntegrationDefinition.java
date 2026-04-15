package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INTEGRATION_DEFINITION.
 * An integration definition entity.
 *
 * @param id STEP instance id
 * @param name integration name
 * @param integrationType integration variance type
 * @param integrationSource integration variance source system
 * @param integrationTarget integration variance target system
 * @param integrationProtocol integration variance protocol
 * @param integrationParameters integration variance parameters
 * @param integrationStatus integration variance status
 */
public record StepIntegrationDefinition(
    int id,
    String name,
    String integrationType,
    String integrationSource,
    String integrationTarget,
    String integrationProtocol,
    List<String> integrationParameters,
    String integrationStatus) implements StepEntity {
}