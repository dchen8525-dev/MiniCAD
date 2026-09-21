package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepIntegrationDefinition extends AbstractStepEntity {
    private final String integrationType;
    private final String integrationSource;
    private final String integrationTarget;
    private final String integrationProtocol;
    private final List<String> integrationParameters;
    private final String integrationStatus;

    public StepIntegrationDefinition(int id, String name, String integrationType, String integrationSource, String integrationTarget, String integrationProtocol, List<String> integrationParameters, String integrationStatus) {
        super(id, name);
        this.integrationType = integrationType;
        this.integrationSource = integrationSource;
        this.integrationTarget = integrationTarget;
        this.integrationProtocol = integrationProtocol;
        this.integrationParameters = integrationParameters == null ? null : java.util.List.copyOf(integrationParameters);
        this.integrationStatus = integrationStatus;
    }

    public String getIntegrationType() {
        return integrationType;
    }

    public String getIntegrationSource() {
        return integrationSource;
    }

    public String getIntegrationTarget() {
        return integrationTarget;
    }

    public String getIntegrationProtocol() {
        return integrationProtocol;
    }

    public List<String> getIntegrationParameters() {
        return integrationParameters;
    }

    public String getIntegrationStatus() {
        return integrationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("integrationType", integrationType);
        state.put("integrationSource", integrationSource);
        state.put("integrationTarget", integrationTarget);
        state.put("integrationProtocol", integrationProtocol);
        state.put("integrationParameters", integrationParameters);
        state.put("integrationStatus", integrationStatus);
        return state;
    }
}
