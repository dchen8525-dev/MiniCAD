package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPortDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String portType;
    private final String portDirection;
    private final String portDataType;
    private final String portProtocol;
    private final String portStatus;

    public StepPortDefinition(int id, String name, String portType, String portDirection, String portDataType, String portProtocol, String portStatus) {
        this.id = id;
        this.name = name;
        this.portType = portType;
        this.portDirection = portDirection;
        this.portDataType = portDataType;
        this.portProtocol = portProtocol;
        this.portStatus = portStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPortDefinition that = (StepPortDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(portType, that.portType) && Objects.equals(portDirection, that.portDirection) && Objects.equals(portDataType, that.portDataType) && Objects.equals(portProtocol, that.portProtocol) && Objects.equals(portStatus, that.portStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, portType, portDirection, portDataType, portProtocol, portStatus);
    }

    @Override
    public String toString() {
        return "StepPortDefinition{" + "id=" + id + "name=" + name + "portType=" + portType + "portDirection=" + portDirection + "portDataType=" + portDataType + "portProtocol=" + portProtocol + "portStatus=" + portStatus + "}";
    }
}