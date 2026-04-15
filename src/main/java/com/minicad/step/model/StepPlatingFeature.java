package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PLATING_FEATURE.
 * A plating feature entity.
 *
 * @param id STEP instance id
 * @param name plating name
 * @param platingType plating type (electroplating, electroless, anodizing)
 * @param platingMaterial plating material specification
 * @param platingThickness plating thickness
 * @param appliedSurfaces surfaces to be plated
 * @param platingParameters plating process parameters
 * @param platingQuality plating quality grade
 */
public record StepPlatingFeature(
    int id,
    String name,
    String platingType,
    StepEntity platingMaterial,
    double platingThickness,
    List<StepEntity> appliedSurfaces,
    List<Double> platingParameters,
    String platingQuality) implements StepEntity {
}