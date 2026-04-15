package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SURFACE_QUALITY.
 * A surface quality entity.
 *
 * @param id STEP instance id
 * @param name quality name
 * @param surface surface reference
 * @param roughnessValues surface roughness values (Ra, Rz, etc.)
 * @param qualityGrade quality grade classification
 * @param measurementMethod measurement method
 * @param direction measurement direction
 */
public record StepSurfaceQuality(
    int id,
    String name,
    StepEntity surface,
    List<Double> roughnessValues,
    String qualityGrade,
    String measurementMethod,
    StepEntity direction) implements StepEntity {
}