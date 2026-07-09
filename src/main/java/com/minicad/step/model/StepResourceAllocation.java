package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RESOURCE_ALLOCATION.
 * A resource allocation entity.
 *
 * @param id STEP instance id
 * @param name allocation name
 * @param allocationType allocation type (machine, tool, personnel)
 * @param allocatedResource allocated resource reference
 * @param allocationQuantity allocation quantity
 * @param allocationPeriod allocation period (start/end times)
 * @variancePriority allocation variance priority
 * @param allocationStatus allocation status
 */
/**
 * Resolved RESOURCE_ALLOCATION.
 * A resource allocation entity.
 *
 * @param id STEP instance id
 * @param name allocation name
 * @param allocationType allocation type (machine, tool, personnel)
 * @param allocatedResource allocated resource reference
 * @param allocationQuantity allocation quantity
 * @param allocationPeriod allocation period (start/end times)
 * @variancePriority allocation variance priority
 * @param allocationStatus allocation status
 */
public final class StepResourceAllocation implements StepEntity {
    private final int id;
    private final String name;
    private final String allocationType;
    private final StepEntity allocatedResource;
    private final int allocationQuantity;
    private final List<Double> allocationPeriod;
    private final int variancePriority;
    private final String allocationStatus;

    public StepResourceAllocation(int id, String name, String allocationType, StepEntity allocatedResource, int allocationQuantity, List<Double> allocationPeriod, int variancePriority, String allocationStatus) {
        this.id = id;
        this.name = name;
        this.allocationType = allocationType;
        this.allocatedResource = allocatedResource;
        this.allocationQuantity = allocationQuantity;
        this.allocationPeriod = allocationPeriod == null ? null : java.util.List.copyOf(allocationPeriod);
        this.variancePriority = variancePriority;
        this.allocationStatus = allocationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAllocationType() {
        return allocationType;
    }

    public StepEntity getAllocatedResource() {
        return allocatedResource;
    }

    public int getAllocationQuantity() {
        return allocationQuantity;
    }

    public List<Double> getAllocationPeriod() {
        return allocationPeriod;
    }

    public int getVariancePriority() {
        return variancePriority;
    }

    public String getAllocationStatus() {
        return allocationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepResourceAllocation that = (StepResourceAllocation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(allocationType, that.allocationType) && Objects.equals(allocatedResource, that.allocatedResource) && allocationQuantity == that.allocationQuantity && Objects.equals(allocationPeriod, that.allocationPeriod) && variancePriority == that.variancePriority && Objects.equals(allocationStatus, that.allocationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, allocationType, allocatedResource, allocationQuantity, allocationPeriod, variancePriority, allocationStatus);
    }

    @Override
    public String toString() {
        return "StepResourceAllocation{" + "id=" + id + "name=" + name + "allocationType=" + allocationType + "allocatedResource=" + allocatedResource + "allocationQuantity=" + allocationQuantity + "allocationPeriod=" + allocationPeriod + "variancePriority=" + variancePriority + "allocationStatus=" + allocationStatus + "}";
    }
}