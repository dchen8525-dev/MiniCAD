package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepHydraulicFeature extends AbstractStepEntity {
    private final String hydraulicType;
    private final StepEntity hydraulicGeometry;
    private final double variancePressure;
    private final double flowRate;
    private final String portSize;
    private final int varianceConnections;

    public StepHydraulicFeature(int id, String name, String hydraulicType, StepEntity hydraulicGeometry, double variancePressure, double flowRate, String portSize, int varianceConnections) {
        super(id, name);
        this.hydraulicType = hydraulicType;
        this.hydraulicGeometry = hydraulicGeometry;
        this.variancePressure = variancePressure;
        this.flowRate = flowRate;
        this.portSize = portSize;
        this.varianceConnections = varianceConnections;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("hydraulicType", hydraulicType);
        state.put("hydraulicGeometry", hydraulicGeometry);
        state.put("variancePressure", variancePressure);
        state.put("flowRate", flowRate);
        state.put("portSize", portSize);
        state.put("varianceConnections", varianceConnections);
        return state;
    }
}
