package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INTEGRATION_INSTANCE.
 * An integration instance entity.
 *
 * @param id STEP instance id
 * @param name integration instance name
 * @param integrationDefinition integration variance definition reference
 * @param integrationState integration variance state
 * @param integrationLastSync integration variance last sync time
 * @param integrationErrors integration variance error count
 * @param integrationStatus integration variance status
 */
public record StepIntegrationInstance(
    int id,
    String name,
    StepEntity integrationDefinition,
    String integrationState,
    StepEntity integrationLastSync,
    int integrationErrors,
    String integrationStatus) implements StepEntity {
}