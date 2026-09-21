package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PORT_DEFINITION.
 * A port definition entity.
 *
 * @param id STEP instance id
 * @param name port name
 * @param portType port variance type
 * @param portDirection port variance direction (input/output/bidirectional)
 * @param portDataType port variance data type
 * @param portProtocol port variance protocol
 * @param portStatus port variance status
 */
public final class StepPortDefinition extends AbstractStepEntity {
    private final String portType;
    private final String portDirection;
    private final String portDataType;
    private final String portProtocol;
    private final String portStatus;

    public StepPortDefinition(int id, String name, String portType, String portDirection, String portDataType, String portProtocol, String portStatus) {
        super(id, name);
        this.portType = portType;
        this.portDirection = portDirection;
        this.portDataType = portDataType;
        this.portProtocol = portProtocol;
        this.portStatus = portStatus;
    }

    public String getPortType() {
        return portType;
    }

    public String getPortDirection() {
        return portDirection;
    }

    public String getPortDataType() {
        return portDataType;
    }

    public String getPortProtocol() {
        return portProtocol;
    }

    public String getPortStatus() {
        return portStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("portType", portType);
        state.put("portDirection", portDirection);
        state.put("portDataType", portDataType);
        state.put("portProtocol", portProtocol);
        state.put("portStatus", portStatus);
        return state;
    }
}
