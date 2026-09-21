package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepServiceDefinition extends AbstractStepEntity {
    private final String serviceType;
    private final String serviceDescription;
    private final StepEntity serviceInterface;
    private final List<StepEntity> serviceDependencies;
    private final String serviceStatus;

    public StepServiceDefinition(int id, String name, String serviceType, String serviceDescription, StepEntity serviceInterface, List<StepEntity> serviceDependencies, String serviceStatus) {
        super(id, name);
        this.serviceType = serviceType;
        this.serviceDescription = serviceDescription;
        this.serviceInterface = serviceInterface;
        this.serviceDependencies = serviceDependencies == null ? null : java.util.List.copyOf(serviceDependencies);
        this.serviceStatus = serviceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("serviceType", serviceType);
        state.put("serviceDescription", serviceDescription);
        state.put("serviceInterface", serviceInterface);
        state.put("serviceDependencies", serviceDependencies);
        state.put("serviceStatus", serviceStatus);
        return state;
    }
}
