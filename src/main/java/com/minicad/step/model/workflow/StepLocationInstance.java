package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
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
public final class StepLocationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity locationDefinition;
    private final String locationState;
    private final double locationCapacity;
    private final String locationStatus;

    public StepLocationInstance(int id, String name, StepEntity locationDefinition, String locationState, double locationCapacity, String locationStatus) {
        this.id = id;
        this.name = name;
        this.locationDefinition = locationDefinition;
        this.locationState = locationState;
        this.locationCapacity = locationCapacity;
        this.locationStatus = locationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLocationInstance that = (StepLocationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(locationDefinition, that.locationDefinition) && Objects.equals(locationState, that.locationState) && locationCapacity == that.locationCapacity && Objects.equals(locationStatus, that.locationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, locationDefinition, locationState, locationCapacity, locationStatus);
    }

    @Override
    public String toString() {
        return "StepLocationInstance{" + "id=" + id + "name=" + name + "locationDefinition=" + locationDefinition + "locationState=" + locationState + "locationCapacity=" + locationCapacity + "locationStatus=" + locationStatus + "}";
    }
}