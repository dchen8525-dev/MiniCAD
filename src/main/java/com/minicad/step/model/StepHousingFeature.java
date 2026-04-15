package com.minicad.step.model;

import java.util.List;

/**
 * Resolved HOUSING_FEATURE.
 * A housing feature entity.
 *
 * @param id STEP instance id
 * @param name housing name
 * @param housingType housing type classification
 * @param bearingSeats bearing seat features
 * @param mountingFeatures mounting features (bolt holes, dowels)
 * @param sealGrooves seal groove features
 * @param housingMaterial housing material specification
 * @param housingGeometry housing geometry representation
 */
public record StepHousingFeature(
    int id,
    String name,
    String housingType,
    List<StepEntity> bearingSeats,
    List<StepEntity> mountingFeatures,
    List<StepEntity> sealGrooves,
    StepEntity housingMaterial,
    StepEntity housingGeometry) implements StepEntity {
}