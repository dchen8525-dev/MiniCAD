package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ZONE_DEFINITION.
 * A zone definition entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param zoneType zone variance type
 * @param zoneLocation zone variance location reference
 * @param zoneBoundary zone variance boundary definition
 * @param zoneStatus zone variance status
 */
public final class StepZoneDefinition extends AbstractStepEntity {
    private final String zoneType;
    private final StepEntity zoneLocation;
    private final String zoneBoundary;
    private final String zoneStatus;

    public StepZoneDefinition(int id, String name, String zoneType, StepEntity zoneLocation, String zoneBoundary, String zoneStatus) {
        super(id, name);
        this.zoneType = zoneType;
        this.zoneLocation = zoneLocation;
        this.zoneBoundary = zoneBoundary;
        this.zoneStatus = zoneStatus;
    }

    public String getZoneType() {
        return zoneType;
    }

    public StepEntity getZoneLocation() {
        return zoneLocation;
    }

    public String getZoneBoundary() {
        return zoneBoundary;
    }

    public String getZoneStatus() {
        return zoneStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("zoneType", zoneType);
        state.put("zoneLocation", zoneLocation);
        state.put("zoneBoundary", zoneBoundary);
        state.put("zoneStatus", zoneStatus);
        return state;
    }
}
