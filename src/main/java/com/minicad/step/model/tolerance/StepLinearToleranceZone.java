package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved LINEAR_TOLERANCE_ZONE.
 * A linear tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneLength length of the tolerance zone
 */
public record StepLinearToleranceZone(
    int id,
    String name,
    StepEntity definingTolerance,
    StepEntity zoneForm,
    Double zoneLength) implements StepEntity {
}