package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepZoneDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String zoneType;
    private final StepEntity zoneLocation;
    private final String zoneBoundary;
    private final String zoneStatus;

    public StepZoneDefinition(int id, String name, String zoneType, StepEntity zoneLocation, String zoneBoundary, String zoneStatus) {
        this.id = id;
        this.name = name;
        this.zoneType = zoneType;
        this.zoneLocation = zoneLocation;
        this.zoneBoundary = zoneBoundary;
        this.zoneStatus = zoneStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepZoneDefinition that = (StepZoneDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(zoneType, that.zoneType) && Objects.equals(zoneLocation, that.zoneLocation) && Objects.equals(zoneBoundary, that.zoneBoundary) && Objects.equals(zoneStatus, that.zoneStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, zoneType, zoneLocation, zoneBoundary, zoneStatus);
    }

    @Override
    public String toString() {
        return "StepZoneDefinition{" + "id=" + id + "name=" + name + "zoneType=" + zoneType + "zoneLocation=" + zoneLocation + "zoneBoundary=" + zoneBoundary + "zoneStatus=" + zoneStatus + "}";
    }
}