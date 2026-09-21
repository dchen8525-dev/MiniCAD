package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepConnectorDefinition extends AbstractStepEntity {
    private final String connectorType;
    private final StepEntity connectorGeometry;
    private final List<StepEntity> connectorPins;
    private final String connectorRating;
    private final String connectorStatus;

    public StepConnectorDefinition(int id, String name, String connectorType, StepEntity connectorGeometry, List<StepEntity> connectorPins, String connectorRating, String connectorStatus) {
        super(id, name);
        this.connectorType = connectorType;
        this.connectorGeometry = connectorGeometry;
        this.connectorPins = connectorPins == null ? null : java.util.List.copyOf(connectorPins);
        this.connectorRating = connectorRating;
        this.connectorStatus = connectorStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("connectorType", connectorType);
        state.put("connectorGeometry", connectorGeometry);
        state.put("connectorPins", connectorPins);
        state.put("connectorRating", connectorRating);
        state.put("connectorStatus", connectorStatus);
        return state;
    }
}
