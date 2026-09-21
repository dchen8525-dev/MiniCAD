package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONNECTION_DEFINITION.
 * A connection definition entity.
 *
 * @param id STEP instance id
 * @param name connection name
 * @param connectionType connection variance type
 * @param connectionProtocol connection variance protocol
 * @param connectionParameters connection variance parameters
 * @param connectionQuality connection variance quality requirements
 * @param connectionStatus connection variance status
 */
public final class StepConnectionDefinition2 extends AbstractStepEntity {
    private final String connectionType;
    private final String connectionProtocol;
    private final List<String> connectionParameters;
    private final String connectionQuality;
    private final String connectionStatus;

    public StepConnectionDefinition2(int id, String name, String connectionType, String connectionProtocol, List<String> connectionParameters, String connectionQuality, String connectionStatus) {
        super(id, name);
        this.connectionType = connectionType;
        this.connectionProtocol = connectionProtocol;
        this.connectionParameters = connectionParameters == null ? null : java.util.List.copyOf(connectionParameters);
        this.connectionQuality = connectionQuality;
        this.connectionStatus = connectionStatus;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getConnectionProtocol() {
        return connectionProtocol;
    }

    public List<String> getConnectionParameters() {
        return connectionParameters;
    }

    public String getConnectionQuality() {
        return connectionQuality;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("connectionType", connectionType);
        state.put("connectionProtocol", connectionProtocol);
        state.put("connectionParameters", connectionParameters);
        state.put("connectionQuality", connectionQuality);
        state.put("connectionStatus", connectionStatus);
        return state;
    }
}
