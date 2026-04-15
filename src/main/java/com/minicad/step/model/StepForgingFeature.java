package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FORGING_FEATURE.
 * A forging feature entity.
 *
 * @param id STEP instance id
 * @param name forging name
 * @param forgingType forging type classification (open die, closed die, upset)
 * @param forgingGeometry forging geometry representation
 * @param dieFlash die flash allowance
 * @param forgingGrain grain direction specification
 * @param forgingMaterial forging material specification
 * @param forgingTemperature forging temperature range
 */
public record StepForgingFeature(
    int id,
    String name,
    String forgingType,
    StepEntity forgingGeometry,
    double dieFlash,
    StepEntity forgingGrain,
    StepEntity forgingMaterial,
    List<Double> forgingTemperature) implements StepEntity {
}