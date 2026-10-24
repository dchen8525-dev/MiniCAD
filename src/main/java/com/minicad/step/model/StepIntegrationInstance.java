package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepIntegrationInstance extends AbstractStepEntity {
    private final StepEntity integrationDefinition;
    private final String integrationState;
    private final StepEntity integrationLastSync;
    private final int integrationErrors;
    private final String integrationStatus;

    public StepIntegrationInstance(int id, String name, StepEntity integrationDefinition, String integrationState, StepEntity integrationLastSync, int integrationErrors, String integrationStatus) {
        super(id, name);
        this.integrationDefinition = integrationDefinition;
        this.integrationState = integrationState;
        this.integrationLastSync = integrationLastSync;
        this.integrationErrors = integrationErrors;
        this.integrationStatus = integrationStatus;
    }

    public StepEntity getIntegrationDefinition() {
        return integrationDefinition;
    }

    public String getIntegrationState() {
        return integrationState;
    }

    public StepEntity getIntegrationLastSync() {
        return integrationLastSync;
    }

    public int getIntegrationErrors() {
        return integrationErrors;
    }

    public String getIntegrationStatus() {
        return integrationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "integrationDefinition", integrationDefinition,
                "integrationState", integrationState, "integrationLastSync", integrationLastSync,
                "integrationErrors", integrationErrors, "integrationStatus", integrationStatus);
    }
}
