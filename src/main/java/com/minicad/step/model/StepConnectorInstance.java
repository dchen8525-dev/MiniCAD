package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepConnectorInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity connectorDefinition;
    private final StepEntity connectorLocation;
    private final String connectorState;
    private final List<String> connectorPinStates;
    private final String connectorStatus;

    public StepConnectorInstance(int id, String name, StepEntity connectorDefinition, StepEntity connectorLocation, String connectorState, List<String> connectorPinStates, String connectorStatus) {
        this.id = id;
        this.name = name;
        this.connectorDefinition = connectorDefinition;
        this.connectorLocation = connectorLocation;
        this.connectorState = connectorState;
        this.connectorPinStates = connectorPinStates == null ? null : java.util.List.copyOf(connectorPinStates);
        this.connectorStatus = connectorStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConnectorInstance that = (StepConnectorInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(connectorDefinition, that.connectorDefinition) && Objects.equals(connectorLocation, that.connectorLocation) && Objects.equals(connectorState, that.connectorState) && Objects.equals(connectorPinStates, that.connectorPinStates) && Objects.equals(connectorStatus, that.connectorStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, connectorDefinition, connectorLocation, connectorState, connectorPinStates, connectorStatus);
    }

    @Override
    public String toString() {
        return "StepConnectorInstance{" + "id=" + id + "name=" + name + "connectorDefinition=" + connectorDefinition + "connectorLocation=" + connectorLocation + "connectorState=" + connectorState + "connectorPinStates=" + connectorPinStates + "connectorStatus=" + connectorStatus + "}";
    }
}