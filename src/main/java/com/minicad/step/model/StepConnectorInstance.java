package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepConnectorInstance extends AbstractStepEntity {
    private final StepEntity connectorDefinition;
    private final StepEntity connectorLocation;
    private final String connectorState;
    private final List<String> connectorPinStates;
    private final String connectorStatus;

    public StepConnectorInstance(int id, String name, StepEntity connectorDefinition, StepEntity connectorLocation, String connectorState, List<String> connectorPinStates, String connectorStatus) {
        super(id, name);
        this.connectorDefinition = connectorDefinition;
        this.connectorLocation = connectorLocation;
        this.connectorState = connectorState;
        this.connectorPinStates = connectorPinStates == null ? null : java.util.List.copyOf(connectorPinStates);
        this.connectorStatus = connectorStatus;
    }

    public StepEntity getConnectorDefinition() {
        return connectorDefinition;
    }

    public StepEntity getConnectorLocation() {
        return connectorLocation;
    }

    public String getConnectorState() {
        return connectorState;
    }

    public List<String> getConnectorPinStates() {
        return connectorPinStates;
    }

    public String getConnectorStatus() {
        return connectorStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("connectorDefinition", connectorDefinition);
        state.put("connectorLocation", connectorLocation);
        state.put("connectorState", connectorState);
        state.put("connectorPinStates", connectorPinStates);
        state.put("connectorStatus", connectorStatus);
        return state;
    }
}
