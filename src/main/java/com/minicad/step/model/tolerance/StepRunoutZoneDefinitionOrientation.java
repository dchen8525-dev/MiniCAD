package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved RUNOUT_ZONE_DEFINITION_ORIENTATION.
 * Defines the orientation of a runout tolerance zone.
 */
public record StepRunoutZoneDefinitionOrientation(
    int id,
    String name,
    StepEntity runoutZone,
    StepEntity orientationAxis,
    Double angle
) implements StepEntity {}
