package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CAPABILITY_PROFILE.
 * A capability profile entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @varianceResource resource variance reference
 * @varianceCapabilities capability variance list
 * @varianceCapacities capacity variance values
 * @varianceAccuracy accuracy variance specifications
 * @varianceStatus profile variance status
 */
public final class StepCapabilityProfile extends AbstractStepEntity {
    private final StepEntity varianceResource;
    private final List<String> varianceCapabilities;
    private final List<Double> varianceCapacities;
    private final List<Double> varianceAccuracy;
    private final String varianceStatus;

    public StepCapabilityProfile(int id, String name, StepEntity varianceResource, List<String> varianceCapabilities, List<Double> varianceCapacities, List<Double> varianceAccuracy, String varianceStatus) {
        super(id, name);
        this.varianceResource = varianceResource;
        this.varianceCapabilities = varianceCapabilities == null ? null : java.util.List.copyOf(varianceCapabilities);
        this.varianceCapacities = varianceCapacities == null ? null : java.util.List.copyOf(varianceCapacities);
        this.varianceAccuracy = varianceAccuracy == null ? null : java.util.List.copyOf(varianceAccuracy);
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceResource() {
        return varianceResource;
    }

    public List<String> getVarianceCapabilities() {
        return varianceCapabilities;
    }

    public List<Double> getVarianceCapacities() {
        return varianceCapacities;
    }

    public List<Double> getVarianceAccuracy() {
        return varianceAccuracy;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceResource", varianceResource);
        state.put("varianceCapabilities", varianceCapabilities);
        state.put("varianceCapacities", varianceCapacities);
        state.put("varianceAccuracy", varianceAccuracy);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
