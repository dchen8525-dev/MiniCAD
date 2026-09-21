package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPneumaticFeature extends AbstractStepEntity {
    private final String pneumaticType;
    private final StepEntity pneumaticGeometry;
    private final double variancePressure;
    private final double varianceFlow;
    private final String portSize;
    private final int varianceConnections;

    public StepPneumaticFeature(int id, String name, String pneumaticType, StepEntity pneumaticGeometry, double variancePressure, double varianceFlow, String portSize, int varianceConnections) {
        super(id, name);
        this.pneumaticType = pneumaticType;
        this.pneumaticGeometry = pneumaticGeometry;
        this.variancePressure = variancePressure;
        this.varianceFlow = varianceFlow;
        this.portSize = portSize;
        this.varianceConnections = varianceConnections;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pneumaticType", pneumaticType);
        state.put("pneumaticGeometry", pneumaticGeometry);
        state.put("variancePressure", variancePressure);
        state.put("varianceFlow", varianceFlow);
        state.put("portSize", portSize);
        state.put("varianceConnections", varianceConnections);
        return state;
    }
}
