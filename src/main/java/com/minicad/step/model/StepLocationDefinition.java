package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LOCATION_DEFINITION.
 * A location definition entity.
 *
 * @param id STEP instance id
 * @param name location name
 * @param locationType location variance type
 * @param locationAddress location variance address
 * @param locationCoordinates location variance coordinates
 * @param locationDescription location variance description
 * @param locationStatus location variance status
 */
public final class StepLocationDefinition extends AbstractStepEntity {
    private final String locationType;
    private final StepEntity locationAddress;
    private final List<Double> locationCoordinates;
    private final String locationDescription;
    private final String locationStatus;

    public StepLocationDefinition(int id, String name, String locationType, StepEntity locationAddress, List<Double> locationCoordinates, String locationDescription, String locationStatus) {
        super(id, name);
        this.locationType = locationType;
        this.locationAddress = locationAddress;
        this.locationCoordinates = locationCoordinates == null ? null : java.util.List.copyOf(locationCoordinates);
        this.locationDescription = locationDescription;
        this.locationStatus = locationStatus;
    }

    public String getLocationType() {
        return locationType;
    }

    public StepEntity getLocationAddress() {
        return locationAddress;
    }

    public List<Double> getLocationCoordinates() {
        return locationCoordinates;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public String getLocationStatus() {
        return locationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("locationType", locationType);
        state.put("locationAddress", locationAddress);
        state.put("locationCoordinates", locationCoordinates);
        state.put("locationDescription", locationDescription);
        state.put("locationStatus", locationStatus);
        return state;
    }
}
