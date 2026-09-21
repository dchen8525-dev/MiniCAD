package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRANSPORT_FEATURE.
 * A transport feature entity.
 *
 * @param id STEP instance id
 * @param name transport name
 * @param transportType transport type (conveyor, crane, truck, rail)
 * @param transportGeometry transport geometry representation
 * @varianceCapacity transport variance capacity
 * @param transportRoute transport route/path reference
 * @varianceSpeed transport variance speed
 * @param transportStandard transport standard reference
 */
public final class StepTransportFeature extends AbstractStepEntity {
    private final String transportType;
    private final StepEntity transportGeometry;
    private final double varianceCapacity;
    private final StepEntity transportRoute;
    private final double varianceSpeed;
    private final String transportStandard;

    public StepTransportFeature(int id, String name, String transportType, StepEntity transportGeometry, double varianceCapacity, StepEntity transportRoute, double varianceSpeed, String transportStandard) {
        super(id, name);
        this.transportType = transportType;
        this.transportGeometry = transportGeometry;
        this.varianceCapacity = varianceCapacity;
        this.transportRoute = transportRoute;
        this.varianceSpeed = varianceSpeed;
        this.transportStandard = transportStandard;
    }

    public String getTransportType() {
        return transportType;
    }

    public StepEntity getTransportGeometry() {
        return transportGeometry;
    }

    public double getVarianceCapacity() {
        return varianceCapacity;
    }

    public StepEntity getTransportRoute() {
        return transportRoute;
    }

    public double getVarianceSpeed() {
        return varianceSpeed;
    }

    public String getTransportStandard() {
        return transportStandard;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transportType", transportType);
        state.put("transportGeometry", transportGeometry);
        state.put("varianceCapacity", varianceCapacity);
        state.put("transportRoute", transportRoute);
        state.put("varianceSpeed", varianceSpeed);
        state.put("transportStandard", transportStandard);
        return state;
    }
}
