package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepResourceInstance extends AbstractStepEntity {
    private final StepEntity resourceDefinition;
    private final StepEntity resourceLocation;
    private final String resourceState;
    private final double resourceAllocation;
    private final List<StepEntity> resourceSchedule;
    private final String resourceStatus;

    public StepResourceInstance(int id, String name, StepEntity resourceDefinition, StepEntity resourceLocation, String resourceState, double resourceAllocation, List<StepEntity> resourceSchedule, String resourceStatus) {
        super(id, name);
        this.resourceDefinition = resourceDefinition;
        this.resourceLocation = resourceLocation;
        this.resourceState = resourceState;
        this.resourceAllocation = resourceAllocation;
        this.resourceSchedule = resourceSchedule == null ? null : java.util.List.copyOf(resourceSchedule);
        this.resourceStatus = resourceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("resourceDefinition", resourceDefinition);
        state.put("resourceLocation", resourceLocation);
        state.put("resourceState", resourceState);
        state.put("resourceAllocation", resourceAllocation);
        state.put("resourceSchedule", resourceSchedule);
        state.put("resourceStatus", resourceStatus);
        return state;
    }
}
