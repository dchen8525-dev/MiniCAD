package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepElectricalFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String electricalType;
    private final StepEntity electricalGeometry;
    private final double voltageRating;
    private final double currentRating;
    private final String wireGauge;
    private final int variancePins;

    public StepElectricalFeature(int id, String name, String electricalType, StepEntity electricalGeometry, double voltageRating, double currentRating, String wireGauge, int variancePins) {
        this.id = id;
        this.name = name;
        this.electricalType = electricalType;
        this.electricalGeometry = electricalGeometry;
        this.voltageRating = voltageRating;
        this.currentRating = currentRating;
        this.wireGauge = wireGauge;
        this.variancePins = variancePins;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepElectricalFeature that = (StepElectricalFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(electricalType, that.electricalType) && Objects.equals(electricalGeometry, that.electricalGeometry) && voltageRating == that.voltageRating && currentRating == that.currentRating && Objects.equals(wireGauge, that.wireGauge) && variancePins == that.variancePins;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, electricalType, electricalGeometry, voltageRating, currentRating, wireGauge, variancePins);
    }

    @Override
    public String toString() {
        return "StepElectricalFeature{" + "id=" + id + "name=" + name + "electricalType=" + electricalType + "electricalGeometry=" + electricalGeometry + "voltageRating=" + voltageRating + "currentRating=" + currentRating + "wireGauge=" + wireGauge + "variancePins=" + variancePins + "}";
    }
}