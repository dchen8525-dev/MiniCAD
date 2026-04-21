package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved RUNOUT_ZONE_DEFINITION.
 * Defines the orientation and form of a runout tolerance zone.
 */
public record StepRunoutZoneDefinition(
    int id,
    String name,
    StepEntity zoneForm) implements StepEntity {
}
