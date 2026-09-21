package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved LOCATION_INSTANCE.
 * A location instance entity.
 *
 * @param id STEP instance id
 * @param name location instance name
 * @param locationDefinition location variance definition reference
 * @param locationState location variance state
 * @param locationCapacity location variance capacity
 * @param locationStatus location variance status
 */
public final class StepLocationInstance extends AbstractStepEntity {
    private final StepEntity locationDefinition;
    private final String locationState;
    private final double locationCapacity;
    private final String locationStatus;

    public StepLocationInstance(int id, String name, StepEntity locationDefinition, String locationState, double locationCapacity, String locationStatus) {
        super(id, name);
        this.locationDefinition = locationDefinition;
        this.locationState = locationState;
        this.locationCapacity = locationCapacity;
        this.locationStatus = locationStatus;
    }

    public StepEntity getLocationDefinition() {
        return locationDefinition;
    }

    public String getLocationState() {
        return locationState;
    }

    public double getLocationCapacity() {
        return locationCapacity;
    }

    public String getLocationStatus() {
        return locationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("locationDefinition", locationDefinition);
        state.put("locationState", locationState);
        state.put("locationCapacity", locationCapacity);
        state.put("locationStatus", locationStatus);
        return state;
    }
}
