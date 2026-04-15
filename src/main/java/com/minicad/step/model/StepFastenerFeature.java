package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FASTENER_FEATURE.
 * A fastener feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType feature variance type
 * @param featureGeometry feature variance geometry reference
 * @param featureSpecification feature variance specification reference
 * @param featureStatus feature variance status
 */
public record StepFastenerFeature(
    int id,
    String name,
    String featureType,
    StepEntity featureGeometry,
    StepEntity featureSpecification,
    String featureStatus) implements StepEntity {
}