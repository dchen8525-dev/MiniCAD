package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepLocationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String locationType;
    private final StepEntity locationAddress;
    private final List<Double> locationCoordinates;
    private final String locationDescription;
    private final String locationStatus;

    public StepLocationDefinition(int id, String name, String locationType, StepEntity locationAddress, List<Double> locationCoordinates, String locationDescription, String locationStatus) {
        this.id = id;
        this.name = name;
        this.locationType = locationType;
        this.locationAddress = locationAddress;
        this.locationCoordinates = locationCoordinates == null ? null : java.util.List.copyOf(locationCoordinates);
        this.locationDescription = locationDescription;
        this.locationStatus = locationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLocationDefinition that = (StepLocationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(locationType, that.locationType) && Objects.equals(locationAddress, that.locationAddress) && Objects.equals(locationCoordinates, that.locationCoordinates) && Objects.equals(locationDescription, that.locationDescription) && Objects.equals(locationStatus, that.locationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, locationType, locationAddress, locationCoordinates, locationDescription, locationStatus);
    }

    @Override
    public String toString() {
        return "StepLocationDefinition{" + "id=" + id + "name=" + name + "locationType=" + locationType + "locationAddress=" + locationAddress + "locationCoordinates=" + locationCoordinates + "locationDescription=" + locationDescription + "locationStatus=" + locationStatus + "}";
    }
}