package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepZoneInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity zoneDefinition;
    private final String zoneState;
    private final double zoneOccupancy;
    private final List<StepEntity> zoneResources;
    private final String zoneStatus;

    public StepZoneInstance(int id, String name, StepEntity zoneDefinition, String zoneState, double zoneOccupancy, List<StepEntity> zoneResources, String zoneStatus) {
        this.id = id;
        this.name = name;
        this.zoneDefinition = zoneDefinition;
        this.zoneState = zoneState;
        this.zoneOccupancy = zoneOccupancy;
        this.zoneResources = zoneResources == null ? null : java.util.List.copyOf(zoneResources);
        this.zoneStatus = zoneStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepZoneInstance that = (StepZoneInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(zoneDefinition, that.zoneDefinition) && Objects.equals(zoneState, that.zoneState) && zoneOccupancy == that.zoneOccupancy && Objects.equals(zoneResources, that.zoneResources) && Objects.equals(zoneStatus, that.zoneStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, zoneDefinition, zoneState, zoneOccupancy, zoneResources, zoneStatus);
    }

    @Override
    public String toString() {
        return "StepZoneInstance{" + "id=" + id + "name=" + name + "zoneDefinition=" + zoneDefinition + "zoneState=" + zoneState + "zoneOccupancy=" + zoneOccupancy + "zoneResources=" + zoneResources + "zoneStatus=" + zoneStatus + "}";
    }
}