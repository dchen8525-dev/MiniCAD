package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPortInstance extends AbstractStepEntity {
    private final StepEntity portDefinition;
    private final String portState;
    private final String portValue;
    private final List<StepEntity> portConnections;
    private final String portStatus;

    public StepPortInstance(int id, String name, StepEntity portDefinition, String portState, String portValue, List<StepEntity> portConnections, String portStatus) {
        super(id, name);
        this.portDefinition = portDefinition;
        this.portState = portState;
        this.portValue = portValue;
        this.portConnections = portConnections == null ? null : java.util.List.copyOf(portConnections);
        this.portStatus = portStatus;
    }

    public StepEntity getPortDefinition() {
        return portDefinition;
    }

    public String getPortState() {
        return portState;
    }

    public String getPortValue() {
        return portValue;
    }

    public List<StepEntity> getPortConnections() {
        return portConnections;
    }

    public String getPortStatus() {
        return portStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("portDefinition", portDefinition);
        state.put("portState", portState);
        state.put("portValue", portValue);
        state.put("portConnections", portConnections);
        state.put("portStatus", portStatus);
        return state;
    }
}
