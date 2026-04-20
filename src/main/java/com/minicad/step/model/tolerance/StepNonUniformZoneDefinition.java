package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved NON_UNIFORM_ZONE_DEFINITION.
 * A tolerance zone definition that varies non-uniformly across the feature.
 */
public record StepNonUniformZoneDefinition(
    int id,
    String name,
    String zoneType,
    StepEntity definingCurve,
    Double variationMagnitude
) implements StepEntity {}
