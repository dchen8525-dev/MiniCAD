package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FITTING_FEATURE.
 * A fitting feature entity.
 *
 * @param id STEP instance id
 * @param name fitting name
 * @param fittingType fitting type (elbow, tee, reducer, coupling)
 * @param fittingAngle fitting angle for elbows
 * @param connectionEnds connection end features
 * @param fittingMaterial fitting material specification
 * @param fittingStandard fitting standard specification
 */
public record StepFittingFeature(
    int id,
    String name,
    String fittingType,
    double fittingAngle,
    List<StepEntity> connectionEnds,
    StepEntity fittingMaterial,
    String fittingStandard) implements StepEntity {
}