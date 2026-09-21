package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ELECTRICAL_FEATURE.
 * An electrical feature entity.
 *
 * @param id STEP instance id
 * @param name electrical name
 * @param electricalType electrical feature type (connector, wire, terminal)
 * @param electricalGeometry electrical geometry representation
 * @param voltageRating voltage rating specification
 * @param currentRating current rating specification
 * @param wireGauge wire gauge specification
 * @variancePins variance pins count for connectors
 */
public final class StepElectricalFeature extends AbstractStepEntity {
    private final String electricalType;
    private final StepEntity electricalGeometry;
    private final double voltageRating;
    private final double currentRating;
    private final String wireGauge;
    private final int variancePins;

    public StepElectricalFeature(int id, String name, String electricalType, StepEntity electricalGeometry, double voltageRating, double currentRating, String wireGauge, int variancePins) {
        super(id, name);
        this.electricalType = electricalType;
        this.electricalGeometry = electricalGeometry;
        this.voltageRating = voltageRating;
        this.currentRating = currentRating;
        this.wireGauge = wireGauge;
        this.variancePins = variancePins;
    }

    public String getElectricalType() {
        return electricalType;
    }

    public StepEntity getElectricalGeometry() {
        return electricalGeometry;
    }

    public double getVoltageRating() {
        return voltageRating;
    }

    public double getCurrentRating() {
        return currentRating;
    }

    public String getWireGauge() {
        return wireGauge;
    }

    public int getVariancePins() {
        return variancePins;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("electricalType", electricalType);
        state.put("electricalGeometry", electricalGeometry);
        state.put("voltageRating", voltageRating);
        state.put("currentRating", currentRating);
        state.put("wireGauge", wireGauge);
        state.put("variancePins", variancePins);
        return state;
    }
}
