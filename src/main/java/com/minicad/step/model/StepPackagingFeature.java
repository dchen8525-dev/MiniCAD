package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PACKAGING_FEATURE.
 * A packaging feature entity.
 *
 * @param id STEP instance id
 * @param name packaging name
 * @param packagingType packaging type (box, pallet, crate)
 * @param packagingGeometry packaging geometry representation
 * @param packagingMaterial packaging material specification
 * @param packagingWeight packaging weight
 * @param packagingDimensions packaging dimensions (L, W, H)
 * @param packagingStandard packaging standard reference
 */
public record StepPackagingFeature(
    int id,
    String name,
    String packagingType,
    StepEntity packagingGeometry,
    StepEntity packagingMaterial,
    double packagingWeight,
    List<Double> packagingDimensions,
    String packagingStandard) implements StepEntity {
}