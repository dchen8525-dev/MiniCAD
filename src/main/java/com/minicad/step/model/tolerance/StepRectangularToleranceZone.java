package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved RECTANGULAR_TOLERANCE_ZONE.
 * A rectangular tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneWidth width of the tolerance zone
 * @param zoneHeight height of the tolerance zone
 */
public record StepRectangularToleranceZone(
    int id,
    String name,
    StepEntity definingTolerance,
    StepEntity zoneForm,
    Double zoneWidth,
    Double zoneHeight) implements StepEntity {
}