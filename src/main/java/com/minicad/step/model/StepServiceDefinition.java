package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SERVICE_DEFINITION.
 * A service definition entity.
 *
 * @param id STEP instance id
 * @param name service name
 * @param serviceType service variance type
 * @param serviceDescription service variance description
 * @param serviceInterface service variance interface reference
 * @param serviceDependencies service variance dependencies
 * @param serviceStatus service variance status
 */
/**
 * Resolved SERVICE_DEFINITION.
 * A service definition entity.
 *
 * @param id STEP instance id
 * @param name service name
 * @param serviceType service variance type
 * @param serviceDescription service variance description
 * @param serviceInterface service variance interface reference
 * @param serviceDependencies service variance dependencies
 * @param serviceStatus service variance status
 */
public final class StepServiceDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String serviceType;
    private final String serviceDescription;
    private final StepEntity serviceInterface;
    private final List<StepEntity> serviceDependencies;
    private final String serviceStatus;

    public StepServiceDefinition(int id, String name, String serviceType, String serviceDescription, StepEntity serviceInterface, List<StepEntity> serviceDependencies, String serviceStatus) {
        this.id = id;
        this.name = name;
        this.serviceType = serviceType;
        this.serviceDescription = serviceDescription;
        this.serviceInterface = serviceInterface;
        this.serviceDependencies = serviceDependencies == null ? null : java.util.List.copyOf(serviceDependencies);
        this.serviceStatus = serviceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public StepEntity getServiceInterface() {
        return serviceInterface;
    }

    public List<StepEntity> getServiceDependencies() {
        return serviceDependencies;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepServiceDefinition that = (StepServiceDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(serviceType, that.serviceType) && Objects.equals(serviceDescription, that.serviceDescription) && Objects.equals(serviceInterface, that.serviceInterface) && Objects.equals(serviceDependencies, that.serviceDependencies) && Objects.equals(serviceStatus, that.serviceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, serviceType, serviceDescription, serviceInterface, serviceDependencies, serviceStatus);
    }

    @Override
    public String toString() {
        return "StepServiceDefinition{" + "id=" + id + "name=" + name + "serviceType=" + serviceType + "serviceDescription=" + serviceDescription + "serviceInterface=" + serviceInterface + "serviceDependencies=" + serviceDependencies + "serviceStatus=" + serviceStatus + "}";
    }
}