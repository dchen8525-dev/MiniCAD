package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMPLEX_FEATURE.
 * A complex feature entity combining multiple features.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param componentFeatures component features
 * @param featureType complex feature type classification
 * @param position feature position placement
 * @param orientation feature orientation
 */
public record StepComplexFeature(
    int id,
    String name,
    List<StepEntity> componentFeatures,
    String featureType,
    StepEntity position,
    StepEntity orientation) implements StepEntity {
}