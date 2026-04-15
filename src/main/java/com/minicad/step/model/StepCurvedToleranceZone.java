package com.minicad.step.model;

/**
 * Resolved CURVED_TOLERANCE_ZONE.
 * A curved tolerance zone definition.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param definingTolerance the geometric tolerance defining this zone
 * @param zoneForm the form of the tolerance zone
 * @param zoneCurve the curve defining the tolerance zone shape
 */
public record StepCurvedToleranceZone(
    int id,
    String name,
    StepEntity definingTolerance,
    StepEntity zoneForm,
    StepEntity zoneCurve) implements StepEntity {
}