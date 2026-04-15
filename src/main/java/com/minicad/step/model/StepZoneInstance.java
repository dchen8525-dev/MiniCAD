package com.minicad.step.model;

import java.util.List;

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
public record StepZoneInstance(
    int id,
    String name,
    StepEntity zoneDefinition,
    String zoneState,
    double zoneOccupancy,
    List<StepEntity> zoneResources,
    String zoneStatus) implements StepEntity {
}