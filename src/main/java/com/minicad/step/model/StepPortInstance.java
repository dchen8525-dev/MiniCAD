package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPortInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity portDefinition;
    private final String portState;
    private final String portValue;
    private final List<StepEntity> portConnections;
    private final String portStatus;

    public StepPortInstance(int id, String name, StepEntity portDefinition, String portState, String portValue, List<StepEntity> portConnections, String portStatus) {
        this.id = id;
        this.name = name;
        this.portDefinition = portDefinition;
        this.portState = portState;
        this.portValue = portValue;
        this.portConnections = portConnections == null ? null : java.util.List.copyOf(portConnections);
        this.portStatus = portStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPortInstance that = (StepPortInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(portDefinition, that.portDefinition) && Objects.equals(portState, that.portState) && Objects.equals(portValue, that.portValue) && Objects.equals(portConnections, that.portConnections) && Objects.equals(portStatus, that.portStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, portDefinition, portState, portValue, portConnections, portStatus);
    }

    @Override
    public String toString() {
        return "StepPortInstance{" + "id=" + id + "name=" + name + "portDefinition=" + portDefinition + "portState=" + portState + "portValue=" + portValue + "portConnections=" + portConnections + "portStatus=" + portStatus + "}";
    }
}