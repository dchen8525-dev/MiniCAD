package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SERVICE_INSTANCE.
 * A service instance entity.
 *
 * @param id STEP instance id
 * @param name service instance name
 * @param serviceDefinition service variance definition reference
 * @param serviceState service variance state
 * @param serviceAvailability service variance availability
 * @param serviceResponseTime service variance response time
 * @param serviceStatus service variance status
 */
public final class StepServiceInstance extends AbstractStepEntity {
    private final StepEntity serviceDefinition;
    private final String serviceState;
    private final double serviceAvailability;
    private final double serviceResponseTime;
    private final String serviceStatus;

    public StepServiceInstance(int id, String name, StepEntity serviceDefinition, String serviceState, double serviceAvailability, double serviceResponseTime, String serviceStatus) {
        super(id, name);
        this.serviceDefinition = serviceDefinition;
        this.serviceState = serviceState;
        this.serviceAvailability = serviceAvailability;
        this.serviceResponseTime = serviceResponseTime;
        this.serviceStatus = serviceStatus;
    }

    public StepEntity getServiceDefinition() {
        return serviceDefinition;
    }

    public String getServiceState() {
        return serviceState;
    }

    public double getServiceAvailability() {
        return serviceAvailability;
    }

    public double getServiceResponseTime() {
        return serviceResponseTime;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("serviceDefinition", serviceDefinition);
        state.put("serviceState", serviceState);
        state.put("serviceAvailability", serviceAvailability);
        state.put("serviceResponseTime", serviceResponseTime);
        state.put("serviceStatus", serviceStatus);
        return state;
    }
}
