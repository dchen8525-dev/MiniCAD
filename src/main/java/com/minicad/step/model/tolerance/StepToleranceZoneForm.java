package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved TOLERANCE_ZONE_FORM.
 * Defines the shape of a tolerance zone (e.g., cylindrical, spherical, planar).
 *
 * @param id STEP instance id
 * @param name form name
 * @param zoneShape the shape description for the tolerance zone
 */
public record StepToleranceZoneForm(
    int id,
    String name,
    String zoneShape) implements StepEntity {
}
