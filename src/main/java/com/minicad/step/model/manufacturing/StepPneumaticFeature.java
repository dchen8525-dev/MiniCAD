package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PNEUMATIC_FEATURE.
 * A pneumatic feature entity.
 *
 * @param id STEP instance id
 * @param name pneumatic name
 * @param pneumaticType pneumatic feature type (compressor, valve, cylinder, line)
 * @param pneumaticGeometry pneumatic geometry representation
 * @param variancePressure variance pressure rating
 * @varianceFlow variance flow rate
 * @param portSize port size specification
 * @varianceConnections variance connections count
 */
/**
 * Resolved PNEUMATIC_FEATURE.
 * A pneumatic feature entity.
 *
 * @param id STEP instance id
 * @param name pneumatic name
 * @param pneumaticType pneumatic feature type (compressor, valve, cylinder, line)
 * @param pneumaticGeometry pneumatic geometry representation
 * @param variancePressure variance pressure rating
 * @varianceFlow variance flow rate
 * @param portSize port size specification
 * @varianceConnections variance connections count
 */
public final class StepPneumaticFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String pneumaticType;
    private final StepEntity pneumaticGeometry;
    private final double variancePressure;
    private final double varianceFlow;
    private final String portSize;
    private final int varianceConnections;

    public StepPneumaticFeature(int id, String name, String pneumaticType, StepEntity pneumaticGeometry, double variancePressure, double varianceFlow, String portSize, int varianceConnections) {
        this.id = id;
        this.name = name;
        this.pneumaticType = pneumaticType;
        this.pneumaticGeometry = pneumaticGeometry;
        this.variancePressure = variancePressure;
        this.varianceFlow = varianceFlow;
        this.portSize = portSize;
        this.varianceConnections = varianceConnections;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPneumaticType() {
        return pneumaticType;
    }

    public StepEntity getPneumaticGeometry() {
        return pneumaticGeometry;
    }

    public double getVariancePressure() {
        return variancePressure;
    }

    public double getVarianceFlow() {
        return varianceFlow;
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
        StepPneumaticFeature that = (StepPneumaticFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pneumaticType, that.pneumaticType) && Objects.equals(pneumaticGeometry, that.pneumaticGeometry) && variancePressure == that.variancePressure && varianceFlow == that.varianceFlow && Objects.equals(portSize, that.portSize) && varianceConnections == that.varianceConnections;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pneumaticType, pneumaticGeometry, variancePressure, varianceFlow, portSize, varianceConnections);
    }

    @Override
    public String toString() {
        return "StepPneumaticFeature{" + "id=" + id + "name=" + name + "pneumaticType=" + pneumaticType + "pneumaticGeometry=" + pneumaticGeometry + "variancePressure=" + variancePressure + "varianceFlow=" + varianceFlow + "portSize=" + portSize + "varianceConnections=" + varianceConnections + "}";
    }
}