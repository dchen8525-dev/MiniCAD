package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ZONE_INSTANCE.
 * A zone instance entity.
 *
 * @param id STEP instance id
 * @param name zone instance name
 * @param zoneDefinition zone variance definition reference
 * @param zoneState zone variance state
 * @param zoneOccupancy zone variance occupancy level
 * @param zoneResources zone variance resources within
 * @param zoneStatus zone variance status
 */
public final class StepZoneInstance extends AbstractStepEntity {
    private final StepEntity zoneDefinition;
    private final String zoneState;
    private final double zoneOccupancy;
    private final List<StepEntity> zoneResources;
    private final String zoneStatus;

    public StepZoneInstance(int id, String name, StepEntity zoneDefinition, String zoneState, double zoneOccupancy, List<StepEntity> zoneResources, String zoneStatus) {
        super(id, name);
        this.zoneDefinition = zoneDefinition;
        this.zoneState = zoneState;
        this.zoneOccupancy = zoneOccupancy;
        this.zoneResources = zoneResources == null ? null : java.util.List.copyOf(zoneResources);
        this.zoneStatus = zoneStatus;
    }

    public StepEntity getZoneDefinition() {
        return zoneDefinition;
    }

    public String getZoneState() {
        return zoneState;
    }

    public double getZoneOccupancy() {
        return zoneOccupancy;
    }

    public List<StepEntity> getZoneResources() {
        return zoneResources;
    }

    public String getZoneStatus() {
        return zoneStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("zoneDefinition", zoneDefinition);
        state.put("zoneState", zoneState);
        state.put("zoneOccupancy", zoneOccupancy);
        state.put("zoneResources", zoneResources);
        state.put("zoneStatus", zoneStatus);
        return state;
    }
}
