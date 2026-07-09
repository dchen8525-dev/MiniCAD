package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HYDRAULIC_FEATURE.
 * A hydraulic feature entity.
 *
 * @param id STEP instance id
 * @param name hydraulic name
 * @param hydraulicType hydraulic feature type (pump, valve, cylinder, line)
 * @param hydraulicGeometry hydraulic geometry representation
 * @variancePressure variance pressure rating
 * @param flowRate flow rate specification
 * @param portSize port size specification
 * @varianceConnections variance connections count
 */
/**
 * Resolved HYDRAULIC_FEATURE.
 * A hydraulic feature entity.
 *
 * @param id STEP instance id
 * @param name hydraulic name
 * @param hydraulicType hydraulic feature type (pump, valve, cylinder, line)
 * @param hydraulicGeometry hydraulic geometry representation
 * @variancePressure variance pressure rating
 * @param flowRate flow rate specification
 * @param portSize port size specification
 * @varianceConnections variance connections count
 */
public final class StepHydraulicFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String hydraulicType;
    private final StepEntity hydraulicGeometry;
    private final double variancePressure;
    private final double flowRate;
    private final String portSize;
    private final int varianceConnections;

    public StepHydraulicFeature(int id, String name, String hydraulicType, StepEntity hydraulicGeometry, double variancePressure, double flowRate, String portSize, int varianceConnections) {
        this.id = id;
        this.name = name;
        this.hydraulicType = hydraulicType;
        this.hydraulicGeometry = hydraulicGeometry;
        this.variancePressure = variancePressure;
        this.flowRate = flowRate;
        this.portSize = portSize;
        this.varianceConnections = varianceConnections;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHydraulicType() {
        return hydraulicType;
    }

    public StepEntity getHydraulicGeometry() {
        return hydraulicGeometry;
    }

    public double getVariancePressure() {
        return variancePressure;
    }

    public double getFlowRate() {
        return flowRate;
    }

    public String getPortSize() {
        return portSize;
    }

    public int getVarianceConnections() {
        return varianceConnections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHydraulicFeature that = (StepHydraulicFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(hydraulicType, that.hydraulicType) && Objects.equals(hydraulicGeometry, that.hydraulicGeometry) && variancePressure == that.variancePressure && flowRate == that.flowRate && Objects.equals(portSize, that.portSize) && varianceConnections == that.varianceConnections;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hydraulicType, hydraulicGeometry, variancePressure, flowRate, portSize, varianceConnections);
    }

    @Override
    public String toString() {
        return "StepHydraulicFeature{" + "id=" + id + "name=" + name + "hydraulicType=" + hydraulicType + "hydraulicGeometry=" + hydraulicGeometry + "variancePressure=" + variancePressure + "flowRate=" + flowRate + "portSize=" + portSize + "varianceConnections=" + varianceConnections + "}";
    }
}