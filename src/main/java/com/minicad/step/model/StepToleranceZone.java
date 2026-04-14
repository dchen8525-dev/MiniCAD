package com.minicad.step.model;

/**
 * Resolved TOLERANCE_ZONE.
 * Defines a tolerance zone with specific form and appearance.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param form tolerance zone form reference
 */
public record StepToleranceZone(
    int id,
    String name,
    StepEntity form) implements StepEntity {
}
