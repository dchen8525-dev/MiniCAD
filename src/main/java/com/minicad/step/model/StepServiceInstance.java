package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepServiceInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity serviceDefinition;
    private final String serviceState;
    private final double serviceAvailability;
    private final double serviceResponseTime;
    private final String serviceStatus;

    public StepServiceInstance(int id, String name, StepEntity serviceDefinition, String serviceState, double serviceAvailability, double serviceResponseTime, String serviceStatus) {
        this.id = id;
        this.name = name;
        this.serviceDefinition = serviceDefinition;
        this.serviceState = serviceState;
        this.serviceAvailability = serviceAvailability;
        this.serviceResponseTime = serviceResponseTime;
        this.serviceStatus = serviceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepServiceInstance that = (StepServiceInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(serviceDefinition, that.serviceDefinition) && Objects.equals(serviceState, that.serviceState) && serviceAvailability == that.serviceAvailability && serviceResponseTime == that.serviceResponseTime && Objects.equals(serviceStatus, that.serviceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, serviceDefinition, serviceState, serviceAvailability, serviceResponseTime, serviceStatus);
    }

    @Override
    public String toString() {
        return "StepServiceInstance{" + "id=" + id + "name=" + name + "serviceDefinition=" + serviceDefinition + "serviceState=" + serviceState + "serviceAvailability=" + serviceAvailability + "serviceResponseTime=" + serviceResponseTime + "serviceStatus=" + serviceStatus + "}";
    }
}