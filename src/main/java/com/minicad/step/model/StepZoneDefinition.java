package com.minicad.step.model;

import java.util.List;

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
public record StepZoneDefinition(
    int id,
    String name,
    String zoneType,
    StepEntity zoneLocation,
    String zoneBoundary,
    String zoneStatus) implements StepEntity {
}