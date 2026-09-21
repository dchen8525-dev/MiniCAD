package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("integrationDefinition", integrationDefinition);
        state.put("integrationState", integrationState);
        state.put("integrationLastSync", integrationLastSync);
        state.put("integrationErrors", integrationErrors);
        state.put("integrationStatus", integrationStatus);
        return state;
    }
}
