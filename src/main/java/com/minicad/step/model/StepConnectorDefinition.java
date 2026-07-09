package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONNECTOR_DEFINITION.
 * A connector definition entity.
 *
 * @param id STEP instance id
 * @param name connector name
 * @param connectorType connector variance type
 * @param connectorGeometry connector variance geometry reference
 * @param connectorPins connector variance pin definitions
 * @param connectorRating connector variance electrical rating
 * @param connectorStatus connector variance status
 */
/**
 * Resolved CONNECTOR_DEFINITION.
 * A connector definition entity.
 *
 * @param id STEP instance id
 * @param name connector name
 * @param connectorType connector variance type
 * @param connectorGeometry connector variance geometry reference
 * @param connectorPins connector variance pin definitions
 * @param connectorRating connector variance electrical rating
 * @param connectorStatus connector variance status
 */
public final class StepConnectorDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String connectorType;
    private final StepEntity connectorGeometry;
    private final List<StepEntity> connectorPins;
    private final String connectorRating;
    private final String connectorStatus;

    public StepConnectorDefinition(int id, String name, String connectorType, StepEntity connectorGeometry, List<StepEntity> connectorPins, String connectorRating, String connectorStatus) {
        this.id = id;
        this.name = name;
        this.connectorType = connectorType;
        this.connectorGeometry = connectorGeometry;
        this.connectorPins = connectorPins == null ? null : java.util.List.copyOf(connectorPins);
        this.connectorRating = connectorRating;
        this.connectorStatus = connectorStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public StepEntity getConnectorGeometry() {
        return connectorGeometry;
    }

    public List<StepEntity> getConnectorPins() {
        return connectorPins;
    }

    public String getConnectorRating() {
        return connectorRating;
    }

    public String getConnectorStatus() {
        return connectorStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConnectorDefinition that = (StepConnectorDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(connectorType, that.connectorType) && Objects.equals(connectorGeometry, that.connectorGeometry) && Objects.equals(connectorPins, that.connectorPins) && Objects.equals(connectorRating, that.connectorRating) && Objects.equals(connectorStatus, that.connectorStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, connectorType, connectorGeometry, connectorPins, connectorRating, connectorStatus);
    }

    @Override
    public String toString() {
        return "StepConnectorDefinition{" + "id=" + id + "name=" + name + "connectorType=" + connectorType + "connectorGeometry=" + connectorGeometry + "connectorPins=" + connectorPins + "connectorRating=" + connectorRating + "connectorStatus=" + connectorStatus + "}";
    }
}