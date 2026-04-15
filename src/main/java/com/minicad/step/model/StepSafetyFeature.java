package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SAFETY_FEATURE.
 * A safety feature entity.
 *
 * @param id STEP instance id
 * @param name safety name
 * @param safetyType safety type (guard, interlock, emergency stop, warning)
 * @param safetyGeometry safety geometry representation
 * @param safetyZone safety zone specification
 * @param safetyClass safety classification level
 * @param safetyStandard safety standard reference
 */
public record StepSafetyFeature(
    int id,
    String name,
    String safetyType,
    StepEntity safetyGeometry,
    StepEntity safetyZone,
    String safetyClass,
    String safetyStandard) implements StepEntity {
}