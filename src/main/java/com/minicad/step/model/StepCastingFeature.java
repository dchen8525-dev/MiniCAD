package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CASTING_FEATURE.
 * A casting feature entity.
 *
 * @param id STEP instance id
 * @param name casting name
 * @param castingType casting type classification (sand, investment, die casting)
 * @param moldGeometry mold geometry representation
 * @param gatingSystem gating system features
 * @param riserFeatures riser/feeder features
 * @param partingSurface parting surface geometry
 * @param castingMaterial casting material specification
 */
public record StepCastingFeature(
    int id,
    String name,
    String castingType,
    StepEntity moldGeometry,
    List<StepEntity> gatingSystem,
    List<StepEntity> riserFeatures,
    StepEntity partingSurface,
    StepEntity castingMaterial) implements StepEntity {
}