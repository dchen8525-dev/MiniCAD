package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RESOURCE_INSTANCE.
 * A resource instance entity.
 *
 * @param id STEP instance id
 * @param name resource instance name
 * @param resourceDefinition resource variance definition reference
 * @param resourceLocation resource variance location reference
 * @param resourceState resource variance state
 * @param resourceAllocation resource variance allocation percentage
 * @param resourceSchedule resource variance schedule entries
 * @param resourceStatus resource variance status
 */
/**
 * Resolved RESOURCE_INSTANCE.
 * A resource instance entity.
 *
 * @param id STEP instance id
 * @param name resource instance name
 * @param resourceDefinition resource variance definition reference
 * @param resourceLocation resource variance location reference
 * @param resourceState resource variance state
 * @param resourceAllocation resource variance allocation percentage
 * @param resourceSchedule resource variance schedule entries
 * @param resourceStatus resource variance status
 */
public final class StepResourceInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity resourceDefinition;
    private final StepEntity resourceLocation;
    private final String resourceState;
    private final double resourceAllocation;
    private final List<StepEntity> resourceSchedule;
    private final String resourceStatus;

    public StepResourceInstance(int id, String name, StepEntity resourceDefinition, StepEntity resourceLocation, String resourceState, double resourceAllocation, List<StepEntity> resourceSchedule, String resourceStatus) {
        this.id = id;
        this.name = name;
        this.resourceDefinition = resourceDefinition;
        this.resourceLocation = resourceLocation;
        this.resourceState = resourceState;
        this.resourceAllocation = resourceAllocation;
        this.resourceSchedule = resourceSchedule == null ? null : java.util.List.copyOf(resourceSchedule);
        this.resourceStatus = resourceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getResourceDefinition() {
        return resourceDefinition;
    }

    public StepEntity getResourceLocation() {
        return resourceLocation;
    }

    public String getResourceState() {
        return resourceState;
    }

    public double getResourceAllocation() {
        return resourceAllocation;
    }

    public List<StepEntity> getResourceSchedule() {
        return resourceSchedule;
    }

    public String getResourceStatus() {
        return resourceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepResourceInstance that = (StepResourceInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(resourceDefinition, that.resourceDefinition) && Objects.equals(resourceLocation, that.resourceLocation) && Objects.equals(resourceState, that.resourceState) && resourceAllocation == that.resourceAllocation && Objects.equals(resourceSchedule, that.resourceSchedule) && Objects.equals(resourceStatus, that.resourceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, resourceDefinition, resourceLocation, resourceState, resourceAllocation, resourceSchedule, resourceStatus);
    }

    @Override
    public String toString() {
        return "StepResourceInstance{" + "id=" + id + "name=" + name + "resourceDefinition=" + resourceDefinition + "resourceLocation=" + resourceLocation + "resourceState=" + resourceState + "resourceAllocation=" + resourceAllocation + "resourceSchedule=" + resourceSchedule + "resourceStatus=" + resourceStatus + "}";
    }
}