package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved RADIAL_TOLERANCE_ZONE.
 * A radial tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneRadius radius of the tolerance zone
 */
public record StepRadialToleranceZone(
    int id,
    String name,
    StepEntity definingTolerance,
    StepEntity zoneForm,
    Double zoneRadius) implements StepEntity {
}