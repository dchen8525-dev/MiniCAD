package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SYSTEM_ARCHITECTURE.
 * A system architecture entity.
 *
 * @param id STEP instance id
 * @param name architecture name
 * @varianceComponents architecture variance components
 * @varianceConnections architecture variance connections
 * @varianceInterfaces architecture variance interfaces
 * @varianceHierarchy architecture variance hierarchy/levels
 * @varianceType architecture variance type (functional, physical, logical)
 * @varianceStatus architecture variance status
 */
public final class StepSystemArchitecture extends AbstractStepEntity {
    private final List<StepEntity> varianceComponents;
    private final List<StepEntity> varianceConnections;
    private final List<StepEntity> varianceInterfaces;
    private final int varianceHierarchy;
    private final String varianceType;
    private final String varianceStatus;

    public StepSystemArchitecture(int id, String name, List<StepEntity> varianceComponents, List<StepEntity> varianceConnections, List<StepEntity> varianceInterfaces, int varianceHierarchy, String varianceType, String varianceStatus) {
        super(id, name);
        this.varianceComponents = varianceComponents == null ? null : java.util.List.copyOf(varianceComponents);
        this.varianceConnections = varianceConnections == null ? null : java.util.List.copyOf(varianceConnections);
        this.varianceInterfaces = varianceInterfaces == null ? null : java.util.List.copyOf(varianceInterfaces);
        this.varianceHierarchy = varianceHierarchy;
        this.varianceType = varianceType;
        this.varianceStatus = varianceStatus;
    }

    public List<StepEntity> getVarianceComponents() {
        return varianceComponents;
    }

    public List<StepEntity> getVarianceConnections() {
        return varianceConnections;
    }

    public List<StepEntity> getVarianceInterfaces() {
        return varianceInterfaces;
    }

    public int getVarianceHierarchy() {
        return varianceHierarchy;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceComponents", varianceComponents);
        state.put("varianceConnections", varianceConnections);
        state.put("varianceInterfaces", varianceInterfaces);
        state.put("varianceHierarchy", varianceHierarchy);
        state.put("varianceType", varianceType);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
