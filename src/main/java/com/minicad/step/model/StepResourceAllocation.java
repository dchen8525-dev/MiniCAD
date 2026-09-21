package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepResourceAllocation extends AbstractStepEntity {
    private final String allocationType;
    private final StepEntity allocatedResource;
    private final int allocationQuantity;
    private final List<Double> allocationPeriod;
    private final int variancePriority;
    private final String allocationStatus;

    public StepResourceAllocation(int id, String name, String allocationType, StepEntity allocatedResource, int allocationQuantity, List<Double> allocationPeriod, int variancePriority, String allocationStatus) {
        super(id, name);
        this.allocationType = allocationType;
        this.allocatedResource = allocatedResource;
        this.allocationQuantity = allocationQuantity;
        this.allocationPeriod = allocationPeriod == null ? null : java.util.List.copyOf(allocationPeriod);
        this.variancePriority = variancePriority;
        this.allocationStatus = allocationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("allocationType", allocationType);
        state.put("allocatedResource", allocatedResource);
        state.put("allocationQuantity", allocationQuantity);
        state.put("allocationPeriod", allocationPeriod);
        state.put("variancePriority", variancePriority);
        state.put("allocationStatus", allocationStatus);
        return state;
    }
}
